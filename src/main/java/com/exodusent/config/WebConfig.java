package com.exodusent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 애플리케이션의 웹 MVC 관련 설정(CORS 등)을 담당하는 설정 클래스.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 모든 경로에 대해 도메인, HTTP 메서드, 헤더를 허용하는 전역 CORS 정책 등록
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // fixme: 배포 URL로 수정할 것
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
