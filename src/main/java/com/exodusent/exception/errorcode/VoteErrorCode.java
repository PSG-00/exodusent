package com.exodusent.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 투표 도메인 전용 에러 코드 열거형.
 */
@Getter
@RequiredArgsConstructor
public enum VoteErrorCode implements ErrorCode {

    ALREADY_VOTED(HttpStatus.CONFLICT, "V001", "이미 투표에 참여한 식별자입니다."),
    INVALID_CHOICE(HttpStatus.BAD_REQUEST, "V002", "유효하지 않은 투표 선택지입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
