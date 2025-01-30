package com.codeit.moim.presentation.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MoimResponse {
    private Long moimId;
    private String title;
    private String moimType;
    private String moimStatus;
    private String si;
    private String district;
    private String roadAddress;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer participants;
    private Integer likes;
}
