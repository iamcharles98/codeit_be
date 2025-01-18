package com.codeit.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseResponse<T> {
    private final Boolean isSuccess;
    private final String message;
    private final int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> BaseResponse<T> success(T result) {
        return new BaseResponse<>(Boolean.TRUE, HttpStatus.OK.toString(), HttpStatus.OK.value(), result);
    }

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(Boolean.TRUE, HttpStatus.OK.toString(), HttpStatus.OK.value());
    }

    public static  <T> BaseResponse<T> fail(ErrorType errorType) {
        return new BaseResponse<>(Boolean.FALSE, errorType.getMessage(), errorType.getHttpStatus());
    }

}
