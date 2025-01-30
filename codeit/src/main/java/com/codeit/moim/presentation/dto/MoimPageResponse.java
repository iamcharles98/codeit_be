package com.codeit.moim.presentation.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MoimPageResponse {
    private List<MoimResponse> content;
    private int currentPage;
    private int totalPages;
}
