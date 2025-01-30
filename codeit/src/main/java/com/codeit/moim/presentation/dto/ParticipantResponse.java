package com.codeit.moim.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ParticipantResponse {
    private String nickName;
    private String position;
    private String image;
}
