package com.codeit.review.presentation;

import com.codeit.review.presentation.dto.MyReviewResponse;
import com.codeit.review.presentation.dto.ReviewCreateRequest;
import com.codeit.review.service.ReviewService;
import com.codeit.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/save")
    @Operation(summary = "리뷰 작성")
    public ResponseEntity<BaseResponse<Long>> save(@RequestAttribute(name = "userId") Long userId, @RequestBody @Valid ReviewCreateRequest request) {
        return ResponseEntity.ok(BaseResponse.success(reviewService.save(userId, request)));
    }

    @GetMapping("/my")
    @Operation(summary = "내가 쓴 리뷰 조회")
    public ResponseEntity<BaseResponse<List<MyReviewResponse>>> getAllMyReview(@RequestAttribute(name = "userId") Long userId) {
        return ResponseEntity.ok(BaseResponse.success(reviewService.findAllByUserId(userId)));
    }

}
