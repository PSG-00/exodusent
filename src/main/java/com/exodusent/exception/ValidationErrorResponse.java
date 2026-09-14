package com.exodusent.exception;

import com.exodusent.exception.errorcode.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 요청 데이터 유효성 검증 실패(@Valid) 시 반환되는 에러 응답 DTO.
 */
@Getter
public class ValidationErrorResponse {

    private final int status;
    private final String code;
    private final String message;
    private final Map<String, String> fieldErrors;
    private final LocalDateTime timestamp;

    // 빌더 패턴을 적용한 생성자
    @Builder
    public ValidationErrorResponse(int status, String code, String message, Map<String, String> fieldErrors, LocalDateTime timestamp) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.fieldErrors = fieldErrors;
        this.timestamp = timestamp;
    }

    // ErrorCode 인터페이스 구현체와 필드 에러 목록을 기반으로 검증 에러 응답 객체 생성
    public static ValidationErrorResponse of(ErrorCode errorCode, List<FieldError> fieldErrors) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ValidationErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .fieldErrors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
