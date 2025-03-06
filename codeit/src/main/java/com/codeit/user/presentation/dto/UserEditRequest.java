package com.codeit.user.presentation.dto;

import com.codeit.user.model.Position;
import com.codeit.util.validator.EnumValue;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEditRequest {

    private String nickname;
    @EnumValue(enumClass = Position.class, message = "직군 유형이 잘못되었습니다.", ignoreCase = true)
    private String position;
    private String introduction;
    private MultipartFile profilePhoto;
    private List<String> tags;
}
