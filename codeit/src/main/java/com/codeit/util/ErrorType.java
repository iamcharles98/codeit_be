package com.codeit.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorType {

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 관리자에게 문의바랍니다."),

    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "요청값이 유효하지 않습니다."),

    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드간 오류가 발생하였습니다."),


    JWT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "토큰값이 없습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이름을 가진 태그가 존재하지 않습니다."),

    MOIM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 모임입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatus() {
        return httpStatus.value();
    }
}
