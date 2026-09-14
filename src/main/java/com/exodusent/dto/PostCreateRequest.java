package com.exodusent.dto;

/**
 * 게시글 생성 요청 시 필요한 데이터를 전달받는 DTO 레코드.
 *
 * @param title   게시글 제목
 * @param content 게시글 내용
 */
public record PostCreateRequest(
        String title,
        String content
) {}
