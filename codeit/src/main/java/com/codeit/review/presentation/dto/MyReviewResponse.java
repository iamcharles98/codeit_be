package com.codeit.review.presentation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MyReviewResponse {

    private String moimTitle;
    private LocalDate moimStartDate;
    private LocalDate moimEndDate;
    private String nickname;
    private String contents;
    private String emotion;
    private LocalDate createdAt;

    @Builder
    public MyReviewResponse(String moimTitle, LocalDate moimStartDate, LocalDate moimEndDate,
                            String nickname, String contents, String emotion, LocalDate createdAt) {
        this.moimTitle = moimTitle;
        this.moimStartDate = moimStartDate;
        this.moimEndDate = moimEndDate;
        this.nickname = nickname;
        this.contents = contents;
        this.emotion = emotion;
        this.createdAt = createdAt;
    }
}
