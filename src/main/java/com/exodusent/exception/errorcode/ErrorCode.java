package com.exodusent.exception.errorcode;

import org.springframework.http.HttpStatus;

/**
 * 도메인별 에러 코드 열거형(Enum)이 구현해야 하는 표준 에러 코드 인터페이스.
 */
public interface ErrorCode {

    // HTTP 상태 코드 반환
    HttpStatus getHttpStatus();

    // 로그 및 클라이언트 식별용 고유 에러 코드 문자열 반환 (예: CT01, CM01 등)
    String getCode();

    // 기본 에러 메시지 반환
    String getMessage();
}
