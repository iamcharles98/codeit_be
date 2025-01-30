package com.codeit.user.presentation.dto;

import java.util.Set;
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
    private String introduction;
    private String photoUrl;
    private Set<String> tags;
}
