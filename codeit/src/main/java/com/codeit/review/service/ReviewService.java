package com.codeit.review.service;

import com.codeit.moim.model.Moim;
import com.codeit.moim.repository.MoimRepository;
import com.codeit.review.model.Emotion;
import com.codeit.review.model.Review;
import com.codeit.review.presentation.dto.MyReviewResponse;
import com.codeit.review.presentation.dto.ReviewCreateRequest;
import com.codeit.review.repository.ReviewRepository;
import com.codeit.user.model.User;
import com.codeit.user.repository.UserRepository;
import com.codeit.util.BaseException;
import com.codeit.util.ErrorType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MoimRepository moimRepository;

    @Transactional
    public Long save(Long userId, ReviewCreateRequest request) {
        Moim moim = moimRepository.findById(request.getMoimId())
                .orElseThrow(() -> new BaseException(ErrorType.MOIM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        Review review = Review.builder()
                .content(request.getContent())
                .emotion(Emotion.valueOf(request.getEmotion()))
                .moim(moim)
                .user(user).build();

        return reviewRepository.save(review).getId();
    }

    public List<MyReviewResponse> findAllByUserId(Long userId) {

        List<Review> reviews = reviewRepository.findByUserIdWithUser(userId);

        List<MyReviewResponse> responses = new ArrayList<>();
        if(reviews != null) {
            for(Review review : reviews) {
                MyReviewResponse myReviewResponse = MyReviewResponse.builder()
                        .moimTitle(review.getMoim().getTitle())
                        .moimStartDate(review.getMoim().getStartDate())
                        .moimEndDate(review.getMoim().getStartDate())
                        .emotion(review.getEmotion().toString())
                        .contents(review.getContent())
                        .nickname(review.getUser().getNickname())
                        .createdAt(review.getCreatedAt())
                        .build();

                responses.add(myReviewResponse);
            }
        }
        return responses;
    }
}
