package com.codeit.user.presentation.dto;

import com.codeit.user.model.Position;
import com.codeit.util.validator.EnumValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegisterRequest {
    @NotBlank
    private String nickname;
    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    @EnumValue(enumClass = Position.class, message = "직군 유형이 잘못되었습니다.", ignoreCase = true)
    private String position;
    private String introduction;
    private MultipartFile profilePhoto;
    private List<String> tags;
}
