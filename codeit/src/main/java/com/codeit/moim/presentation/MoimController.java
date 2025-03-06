package com.codeit.moim.presentation;

import com.codeit.moim.presentation.dto.MoimCreateRequest;
import com.codeit.moim.presentation.dto.MoimDetailResponse;
import com.codeit.moim.presentation.dto.MoimPageResponse;
import com.codeit.moim.presentation.dto.MoimResponse;
import com.codeit.moim.service.MoimService;
import com.codeit.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moim")
@Slf4j
public class MoimController {
    private final MoimService moimService;

    @PostMapping("/create")
    @Operation(summary = "모임 생성")
    public ResponseEntity<BaseResponse<Long>> create(@RequestAttribute(name = "userId") Long userId,
                                                     @Valid MoimCreateRequest request) {
        return ResponseEntity.ok(BaseResponse.success(moimService.createMoim(userId, request)));
    }

    @GetMapping("/all")
    @Operation(summary = "모든 모임 조회")
    public ResponseEntity<BaseResponse<MoimPageResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(BaseResponse.success(moimService.findMoims(page, size)));
    }

    @GetMapping("/detail/{moimId}")
    @Operation(summary = "모임 상세 조회")
    public ResponseEntity<BaseResponse<MoimDetailResponse>> getMoimDetail(@PathVariable Long moimId) {
        return ResponseEntity.ok(BaseResponse.success(moimService.getMoim(moimId)));
    }

    @GetMapping("/own")
    @Operation(summary = "내가 만든 모임 조회")
    public ResponseEntity<BaseResponse<List<MoimResponse>>> getOwn(@RequestAttribute(name = "userId") Long userId) {
        return ResponseEntity.ok(BaseResponse.success(moimService.findOwnMoims(userId)));
    }

    @GetMapping("/my")
    @Operation(summary = "내가 참여중인 모임 조회")
    public ResponseEntity<BaseResponse<List<MoimResponse>>> getMy(@RequestAttribute(name = "userId") Long userId) {
        return ResponseEntity.ok(BaseResponse.success(moimService.findParticipatingMoims(userId)));
    }

    @PostMapping("/join")
    @Operation(summary = "모임 참여")
    public ResponseEntity<BaseResponse<Boolean>> join(@RequestAttribute(name = "userId") Long userId,
                                                      @RequestParam(name = "moimId") Long moimId) {
        return ResponseEntity.ok(BaseResponse.success(moimService.participate(userId, moimId)));
    }

    @PostMapping("/like")
    @Operation(summary = "모임 찜하기")
    public ResponseEntity<BaseResponse<Boolean>> like(@RequestAttribute(name = "userId") Long userId,
                                                      @RequestParam(name = "moimId") Long moimId) {
        return ResponseEntity.ok(BaseResponse.success(moimService.like(userId, moimId)));
    }

    @DeleteMapping("/dislike")
    @Operation(summary = "모임 찜해제")
    public ResponseEntity<BaseResponse<Boolean>> dislike(@RequestAttribute(name = "userId") Long userId,
                                                         @RequestParam(name = "moimId") Long moimId) {
        return ResponseEntity.ok(BaseResponse.success(moimService.dislike(userId, moimId)));
    }

    @GetMapping("/likes")
    @Operation(summary = "찜한 모임 조회")
    public ResponseEntity<BaseResponse<List<MoimResponse>>> likeMoims(@RequestAttribute(name = "userId") Long userId) {
        return ResponseEntity.ok(BaseResponse.success(moimService.getLikeMoims(userId)));
    }
}
