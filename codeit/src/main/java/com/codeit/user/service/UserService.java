package com.codeit.user.service;

import com.codeit.user.model.Position;
import com.codeit.user.model.User;
import com.codeit.user.presentation.dto.UserDetailResponse;
import com.codeit.user.presentation.dto.UserLoginRequest;
import com.codeit.user.presentation.dto.UserLoginResponse;
import com.codeit.user.presentation.dto.UserRegisterRequest;
import com.codeit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long register(UserRegisterRequest userRegisterRequest) {

        User user = User.builder().email(userRegisterRequest.getEmail())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .nickname(userRegisterRequest.getNickname())
                .position(Position.valueOf(userRegisterRequest.getPosition()))
                .build();

        return userRepository.save(user).getId();
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {

        User user = userRepository.findByEmail(userLoginRequest.getEmail()).
                orElseThrow(() -> new RuntimeException("User not found"));

        if(!isPasswordMatch(userLoginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        return new UserLoginResponse("access", "refresh");
    }

    public UserDetailResponse getUserDetail(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDetailResponse(user.getId(), user.getNickname(), user.getPosition().toString());
    }

    private boolean isPasswordMatch(String input, String encryptedPassword) {
        return passwordEncoder.matches(input, encryptedPassword);
    }
}
