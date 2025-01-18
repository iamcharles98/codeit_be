package com.codeit.user.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDetailResponse {

    private Long id;
    private String nickname;
    private String position;
}
