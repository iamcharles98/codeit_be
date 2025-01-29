package com.codeit.user.service;


import com.codeit.jwt.JwtTokenProvider;
import com.codeit.user.model.Position;
import com.codeit.user.model.Tag;
import com.codeit.user.model.User;
import com.codeit.user.presentation.dto.UserDetailResponse;
import com.codeit.user.presentation.dto.UserLoginRequest;
import com.codeit.user.presentation.dto.UserLoginResponse;
import com.codeit.user.presentation.dto.UserRegisterRequest;
import com.codeit.user.presentation.dto.UserTagRequest;
import com.codeit.user.repository.TagRepository;
import com.codeit.user.repository.UserRepository;
import com.codeit.util.BaseException;
import com.codeit.util.ErrorType;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long register(UserRegisterRequest userRegisterRequest) {

        User user = User.builder().email(userRegisterRequest.getEmail())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .nickname(userRegisterRequest.getNickname())
                .position(Position.valueOf(userRegisterRequest.getPosition()))
                .introduction(userRegisterRequest.getIntroduction())
                .tags(new HashSet<>())
                .build();

        for (String tagName : userRegisterRequest.getTags()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));

            user.addTag(tag);
        }

        return userRepository.save(user).getId();
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {

        User user = userRepository.findByEmail(userLoginRequest.getEmail()).
                orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        if (!isPasswordMatch(userLoginRequest.getPassword(), user.getPassword())) {
            throw new BaseException(ErrorType.INVALID_PASSWORD);
        }

        return new UserLoginResponse(jwtTokenProvider.createAccessToken(user.getId()),
                jwtTokenProvider.createRefreshToken(user.getId()));
    }

    public UserDetailResponse getUserDetail(Long id) {
        User user = userRepository.findByIdWithTags(id)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        return new UserDetailResponse(user.getId(), user.getNickname(), user.getPosition().toString(),
                user.getIntroduction(), user.getTags());
    }

    @Transactional
    public void removeTagFromUser(Long userId, String tagName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));

        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new BaseException(ErrorType.TAG_NOT_FOUND));

        user.removeTag(tag);
        userRepository.save(user);
    }

    @Transactional
    public void addTagToUser(Long userId, String tagName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));
        Tag tag = tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName)));

        user.addTag(tag);
        userRepository.save(user);

    }

    private boolean isPasswordMatch(String input, String encryptedPassword) {
        return passwordEncoder.matches(input, encryptedPassword);
    }
}
