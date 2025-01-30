package com.codeit.moim.presentation.dto;

import com.codeit.moim.model.MoimType;
import com.codeit.util.validator.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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
public class MoimCreateRequest {
    @EnumValue(enumClass = MoimType.class, message = "모임 유형이 올바르지 않습니다.", ignoreCase = true)
    private String type;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String si;
    @NotBlank
    private String district;
    @NotBlank
    private String roadAddress;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentDeadline;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    private Integer minParticipants;
    @NotNull
    private Integer maxParticipants;
    List<MultipartFile> photos;
}
