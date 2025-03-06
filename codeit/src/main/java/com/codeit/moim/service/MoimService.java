package com.codeit.moim.service;

import com.codeit.config.S3Uploader;
import com.codeit.moim.model.LikeMoim;
import com.codeit.moim.model.Moim;
import com.codeit.moim.model.MoimLocation;
import com.codeit.moim.model.MoimPhoto;
import com.codeit.moim.model.MoimStatus;
import com.codeit.moim.model.MoimType;
import com.codeit.moim.model.ParticipatingMoim;
import com.codeit.moim.presentation.dto.MoimCreateRequest;
import com.codeit.moim.presentation.dto.MoimDetailResponse;
import com.codeit.moim.presentation.dto.MoimPageResponse;
import com.codeit.moim.presentation.dto.MoimResponse;
import com.codeit.moim.presentation.dto.MoimReviewResponse;
import com.codeit.moim.presentation.dto.ParticipantResponse;
import com.codeit.moim.repository.LikeMoimRepository;
import com.codeit.moim.repository.MoimLocationRepository;
import com.codeit.moim.repository.MoimRepository;
import com.codeit.moim.repository.ParticipatingMoimRepository;
import com.codeit.review.model.Review;
import com.codeit.review.repository.ReviewRepository;
import com.codeit.user.model.User;
import com.codeit.user.repository.UserRepository;
import com.codeit.util.BaseException;
import com.codeit.util.ErrorType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MoimService {
    private static final Logger log = LoggerFactory.getLogger(MoimService.class);
    private final MoimRepository moimRepository;
    private final MoimLocationRepository moimLocationRepository;
    private final LikeMoimRepository likeMoimRepository;
    private final ParticipatingMoimRepository participatingMoimRepository;
    private final ReviewRepository reviewRepository;
    private final S3Uploader s3Uploader;
    private final UserRepository userRepository;

    @Transactional
    public Long createMoim(Long userId, MoimCreateRequest request) {

        // MoimLocation 생성
        MoimLocation moimLocation = MoimLocation.builder()
                .si(request.getSi())
                .district(request.getDistrict())
                .roadAddress(request.getRoadAddress()).build();

        // Moim 엔티티 생성
        Moim moim = Moim.builder()
                .moimType(MoimType.valueOf(request.getType()))
                .title(request.getTitle())
                .content(request.getContent())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .recruitmentDeadline(request.getRecruitmentDeadline())
                .moimStatus(MoimStatus.RECRUITING)
                .minParticipants(request.getMinParticipants())
                .maxParticipants(request.getMaxParticipants())
                .moimLocation(moimLocation)
                .moimPhotos(new ArrayList<>())  // 사진 리스트 초기화
                .build();

        // Moim 저장 (현재 Moim의 ID는 아직 DB에 저장되지 않음)
        Moim saved = moimRepository.save(moim);

        // 사진이 있을 경우, MoimPhoto 추가
        if (!request.getPhotos().isEmpty()) {
            for (MultipartFile file : request.getPhotos()) {
                try {
                    String url = s3Uploader.upload(file);
                    // MoimPhoto에 Moim을 설정
                    saved.addPhoto(MoimPhoto.builder().photoName(file.getName()).url(url).moim(saved).build());
                } catch (IOException e) {
                    throw new BaseException(ErrorType.FILE_UPLOAD_ERROR);
                }
            }
        }

        // Moim과 관련된 사진들을 저장 (Cascade 설정이 되어 있으면 MoimPhoto도 자동으로 저장됨)
        moimRepository.save(saved);

        // 유저 찾기 및 참여 정보 저장
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        ParticipatingMoim participatingMoim = ParticipatingMoim.builder()
                .moim(saved)
                .user(user)
                .isHost(true)
                .build();

        participatingMoimRepository.save(participatingMoim);

        // 생성된 Moim의 ID 반환
        return saved.getId();
    }

    public MoimPageResponse findMoims(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Moim> moims = moimRepository.findAllWithLocation(pageable);

        List<MoimResponse> responses = new ArrayList<>();
        for (Moim moim : moims) {
            int participants = participatingMoimRepository.findParticipantsByMoimId(moim.getId());
            int likes = likeMoimRepository.countLikeMoimByMoimId(moim.getId());
            MoimResponse moimResponse = MoimResponse.builder()
                    .moimId(moim.getId())
                    .title(moim.getTitle())
                    .moimStatus(moim.getMoimStatus().toString())
                    .moimType(moim.getMoimType().toString())
                    .si(moim.getMoimLocation().getSi())
                    .district(moim.getMoimLocation().getDistrict())
                    .roadAddress(moim.getMoimLocation().getRoadAddress())
                    .startDate(moim.getStartDate())
                    .endDate(moim.getEndDate())
                    .participants(participants)
                    .likes(likes).build();

            responses.add(moimResponse);
        }

        return new MoimPageResponse(responses, moims.getNumber(), moims.getTotalPages());
    }

    public MoimDetailResponse getMoim(Long moimId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(() -> new BaseException(ErrorType.MOIM_NOT_FOUND));

        List<Review> reviews = reviewRepository.findByMoimIdWithUser(moimId);

        List<MoimReviewResponse> reviewResponses = reviews.stream()
                .map(r -> new MoimReviewResponse(r.getUser().getNickname(), r.getUser().getProfilePhotoUrl(),
                        r.getContent(), r.getEmotion().toString(),
                        r.getCreatedAt().toLocalDate())).toList();

        List<ParticipatingMoim> participants = participatingMoimRepository.findAllParticipantsByMoimId(moimId);
        List<ParticipantResponse> participantResponses = participants.stream()
                .map(p -> new ParticipantResponse(p.getUser().getNickname(), p.getUser().getPosition().toString(),
                        p.getUser().getProfilePhotoUrl())).toList();
        int likes = likeMoimRepository.countLikeMoimByMoimId(moimId);
        return MoimDetailResponse.builder()
                .moimId(moim.getId())
                .title(moim.getTitle())
                .content(moim.getContent())
                .moimType(moim.getMoimType().toString())
                .moimStatus(moim.getMoimStatus().toString())
                .si(moim.getMoimLocation().getSi())
                .district(moim.getMoimLocation().getDistrict())
                .roadAddress(moim.getMoimLocation().getRoadAddress())
                .startDate(moim.getStartDate())
                .endDate(moim.getEndDate())
                .participantCnt(participants.size())
                .photos(moim.getMoimPhotos().stream().map(MoimPhoto::getUrl).toList())
                .likes(likes)
                .minParticipants(moim.getMinParticipants())
                .maxParticipants(moim.getMaxParticipants())
                .reviews(reviewResponses)
                .participants(participantResponses)
                .build();
    }

    public List<MoimResponse> findOwnMoims(Long userId) {
        List<ParticipatingMoim> hostingMoims = participatingMoimRepository.findHostingMoimsByUserId(userId);

        List<MoimResponse> responses = new ArrayList<>();
        for (ParticipatingMoim hostingMoim : hostingMoims) {
            Moim moim = hostingMoim.getMoim();
            int participants = participatingMoimRepository.findParticipantsByMoimId(moim.getId());
            int likes = likeMoimRepository.countLikeMoimByMoimId(moim.getId());
            MoimResponse moimResponse = MoimResponse.builder()
                    .moimId(moim.getId())
                    .title(moim.getTitle())
                    .moimStatus(moim.getMoimStatus().toString())
                    .moimType(moim.getMoimType().toString())
                    .si(moim.getMoimLocation().getSi())
                    .district(moim.getMoimLocation().getDistrict())
                    .roadAddress(moim.getMoimLocation().getRoadAddress())
                    .startDate(moim.getStartDate())
                    .endDate(moim.getEndDate())
                    .participants(participants)
                    .likes(likes).build();
            responses.add(moimResponse);

        }
        return responses;
    }

    public List<MoimResponse> findParticipatingMoims(Long userId) {
        List<ParticipatingMoim> participatingMoims = participatingMoimRepository.findParticipatingMoimByUserId(userId);

        List<MoimResponse> responses = new ArrayList<>();
        for (ParticipatingMoim participatingMoim : participatingMoims) {
            Moim moim = participatingMoim.getMoim();
            int participants = participatingMoimRepository.findParticipantsByMoimId(moim.getId());
            int likes = likeMoimRepository.countLikeMoimByMoimId(moim.getId());
            MoimResponse moimResponse = MoimResponse.builder()
                    .moimId(moim.getId())
                    .title(moim.getTitle())
                    .moimStatus(moim.getMoimStatus().toString())
                    .moimType(moim.getMoimType().toString())
                    .si(moim.getMoimLocation().getSi())
                    .district(moim.getMoimLocation().getDistrict())
                    .roadAddress(moim.getMoimLocation().getRoadAddress())
                    .startDate(moim.getStartDate())
                    .endDate(moim.getEndDate())
                    .participants(participants)
                    .likes(likes).build();
            responses.add(moimResponse);
        }
        return responses;
    }

    @Transactional
    public Boolean participate(Long userId, Long moimId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(() -> new BaseException(ErrorType.MOIM_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        ParticipatingMoim participatingMoim = ParticipatingMoim.builder()
                .moim(moim)
                .user(user)
                .isHost(false)
                .build();

        participatingMoimRepository.save(participatingMoim);
        return true;
    }

    @Transactional
    public Boolean like(Long userId, Long moimId) {

        if (likeMoimRepository.findByMoim_IdAndUser_Id(moimId, userId).isPresent()) {
            likeMoimRepository.updateIsDeletedByMoimAndUser(false, moimId, userId);
            return true;
        }
        Moim moim = moimRepository.findById(moimId).orElseThrow(() -> new BaseException(ErrorType.MOIM_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        LikeMoim likeMoim = LikeMoim.builder()
                .moim(moim)
                .user(user)
                .isDeleted(false)
                .build();

        likeMoimRepository.save(likeMoim);
        return true;
    }

    public List<MoimResponse> getLikeMoims(Long userId) {
        List<LikeMoim> likeMoims = likeMoimRepository.findLikeMoimByUserId(userId);

        List<MoimResponse> responses = new ArrayList<>();
        for (LikeMoim likeMoim : likeMoims) {
            Moim moim = likeMoim.getMoim();
            int participants = participatingMoimRepository.findParticipantsByMoimId(moim.getId());
            int likes = likeMoimRepository.countLikeMoimByMoimId(moim.getId());
            MoimResponse moimResponse = MoimResponse.builder()
                    .moimId(moim.getId())
                    .title(moim.getTitle())
                    .moimStatus(moim.getMoimStatus().toString())
                    .moimType(moim.getMoimType().toString())
                    .si(moim.getMoimLocation().getSi())
                    .district(moim.getMoimLocation().getDistrict())
                    .roadAddress(moim.getMoimLocation().getRoadAddress())
                    .startDate(moim.getStartDate())
                    .endDate(moim.getEndDate())
                    .participants(participants)
                    .likes(likes).build();
            responses.add(moimResponse);
        }
        return responses;
    }

    @Transactional
    public Boolean dislike(Long userId, Long moimId) {
        likeMoimRepository.updateIsDeletedByMoimAndUser(true, moimId, userId);
        return true;
    }
}
