package com.exodusent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 애플리케이션의 헬스 상태 및 생존 여부를 점검하기 위한 컨트롤러 클래스.
 */
@RestController
public class HealthController {

    // 서버의 생존 여부 확인을 위해 OK 문자열과 200 OK 응답 반환
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
