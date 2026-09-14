package com.exodusent.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 게시글(Post) 도메인 전용 에러 코드 열거형.
 */
@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "존재하지 않는 게시글입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
