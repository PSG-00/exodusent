package com.exodusent.exception;

import com.exodusent.exception.errorcode.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * 일반 예외 및 비즈니스 예외 발생 시 클라이언트에게 반환되는 표준 에러 응답 DTO.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final int status;
    private final String code;
    private final String message;
    private final Map<String, Object> details;
    private final LocalDateTime timestamp;

    // 빌더 패턴을 적용한 생성자
    @Builder
    public ErrorResponse(int status, String code, String message, Map<String, Object> details, LocalDateTime timestamp) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.details = details != null ? details : Collections.emptyMap();
        this.timestamp = timestamp;
    }

    // ErrorCode 기반 표준 응답 생성 메서드
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ErrorCode 및 상세 정보를 기반으로 응답 생성 메서드
    public static ErrorResponse of(ErrorCode errorCode, Map<String, Object> details) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ErrorCode, 커스텀 메시지 및 상세 정보 기반 응답 생성 메서드
    public static ErrorResponse of(ErrorCode errorCode, String customMessage, Map<String, Object> details) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .message(customMessage)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ErrorCode와 커스텀 메시지 기반 응답 생성 메서드
    public static ErrorResponse of(ErrorCode errorCode, String customMessage) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .message(customMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // HttpStatus 및 메시지 기반 응답 생성 메서드 (예상치 못한 시스템 에러용)
    public static ErrorResponse of(HttpStatus status, String code, String message) {
        return ErrorResponse.builder()
                .status(status.value())
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
