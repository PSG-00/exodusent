package com.exodusent.exception;

import com.exodusent.exception.errorcode.CommonErrorCode;
import com.exodusent.exception.errorcode.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 애플리케이션 전역에서 발생하는 예외를 가로채어 표준화된 응답을 반환하는 컨트롤러 어드바이스 클래스.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 로직 예외(BusinessException) 처리 및 로그/응답에 상세 컨텍스트(details) 반영
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("[BusinessException] Code: {}, Message: {}, Details: {}", errorCode.getCode(), ex.getMessage(), ex.getDetails());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.of(errorCode, ex.getMessage(), ex.getDetails()));
    }

    // Bean Validation 검증 실패(@Valid) 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("[MethodArgumentNotValidException] Code: {}, Message: {}", CommonErrorCode.INVALID_INPUT_VALUE.getCode(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ValidationErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, ex.getBindingResult().getFieldErrors()));
    }

    // 잘못된 인자 전달(IllegalArgumentException) 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("[IllegalArgumentException] Code: {}, Message: {}", CommonErrorCode.INVALID_INPUT_VALUE.getCode(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, ex.getMessage()));
    }

    // 처리되지 않은 시스템 내부 예외(Exception) 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("[UnhandledException] Code: {}, Exception: ", CommonErrorCode.INTERNAL_SERVER_ERROR.getCode(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }
}
