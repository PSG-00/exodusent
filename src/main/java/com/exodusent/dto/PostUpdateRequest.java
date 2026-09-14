package com.exodusent.dto;

/**
 * 게시글 수정 요청 시 필요한 데이터를 전달받는 DTO 레코드.
 *
 * @param title   수정할 게시글 제목
 * @param content 수정할 게시글 내용
 */
public record PostUpdateRequest(
        String title,
        String content
) {}
