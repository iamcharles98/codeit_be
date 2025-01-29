package com.codeit.review.presentation.dto;

import com.codeit.review.model.Emotion;
import com.codeit.util.validator.EnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewCreateRequest {
    private Long moimId;
    @EnumValue(enumClass = Emotion.class, message = "평가 유형이 잘못되었습니다", ignoreCase = true)
    private String emotion;
    @NotBlank
    private String content;
}
