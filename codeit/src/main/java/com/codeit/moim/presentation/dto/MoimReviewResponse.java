package com.codeit.moim.presentation.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MoimReviewResponse {

    private String nickname;
    private String profilePhotoUrl;
    private String contents;
    private String emotion;
    private LocalDate createdAt;
}
