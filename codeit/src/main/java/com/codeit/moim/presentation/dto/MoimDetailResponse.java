package com.codeit.moim.presentation.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MoimDetailResponse {
    private Long moimId;
    private String title;
    private String content;
    private String moimType;
    private String moimStatus;
    private String si;
    private String district;
    private String roadAddress;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer participantCnt;
    private Integer likes;
    private Integer minParticipants;
    private Integer maxParticipants;
    private List<String> photos;
    private List<MoimReviewResponse> reviews;
    private List<ParticipantResponse> participants;
}
