package com.exodusent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exodusent 애플리케이션의 시작점(Entry point) 클래스.
 */
@SpringBootApplication
public class ExodusentApplication {

    // Spring Boot 애플리케이션 구동 진입 메서드
    public static void main(String[] args) {
        SpringApplication.run(ExodusentApplication.class, args);
    }

}
