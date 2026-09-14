package com.exodusent.exception;

import com.exodusent.exception.errorcode.ErrorCode;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 비즈니스 로직 수행 중 발생하는 공통 도메인 예외 클래스.
 * 에러 코드와 함께 디버깅 및 추적을 위한 상세 컨텍스트(details) 정보를 보관합니다.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    // ErrorCode 기반 기본 생성자 (상세 정보 없음)
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = Collections.emptyMap();
    }

    // ErrorCode와 상세 컨텍스트 Map을 지정하는 생성자
    public BusinessException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details != null
                ? Collections.unmodifiableMap(new HashMap<>(details))
                : Collections.emptyMap();
    }

    // 단일 키-값 상세 정보를 간편하게 전달하기 위한 편의 생성자
    public BusinessException(ErrorCode errorCode, String detailKey, Object detailValue) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = Collections.singletonMap(detailKey, detailValue);
    }

    // ErrorCode, 상세 정보 및 원인 예외(cause)를 지정하는 생성자
    public BusinessException(ErrorCode errorCode, Map<String, Object> details, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.details = details != null
                ? Collections.unmodifiableMap(new HashMap<>(details))
                : Collections.emptyMap();
    }

    // 커스텀 메시지를 사용하는 생성자
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = Collections.emptyMap();
    }

    // 커스텀 메시지와 상세 정보를 함께 지정하는 생성자
    public BusinessException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details != null
                ? Collections.unmodifiableMap(new HashMap<>(details))
                : Collections.emptyMap();
    }
}
