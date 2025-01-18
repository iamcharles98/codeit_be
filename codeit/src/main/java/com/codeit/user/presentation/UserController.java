package com.codeit.user.presentation;

import com.codeit.user.presentation.dto.UserDetailResponse;
import com.codeit.user.presentation.dto.UserLoginRequest;
import com.codeit.user.presentation.dto.UserLoginResponse;
import com.codeit.user.presentation.dto.UserRegisterRequest;
import com.codeit.user.service.UserService;
import com.codeit.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "회원가입")
    public ResponseEntity<BaseResponse<Long>> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        log.info(userRegisterRequest.toString());
        return ResponseEntity.ok(BaseResponse.success(userService.register(userRegisterRequest)));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<BaseResponse<UserLoginResponse>> login(
            @RequestBody @Valid UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(BaseResponse.success(userService.login(userLoginRequest)));
    }

    @GetMapping("/detail")
    @Operation(summary = "유저 정보 조회 (마이페이지)")
    public ResponseEntity<BaseResponse<UserDetailResponse>> detail(@RequestParam Long id) {
        return ResponseEntity.ok(BaseResponse.success(userService.getUserDetail(id)));
    }
}
