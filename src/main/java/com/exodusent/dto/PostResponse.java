package com.exodusent.dto;

import com.exodusent.entity.Post;

/**
 * 게시글 조회 결과를 클라이언트에게 전달하기 위한 응답 DTO 레코드.
 *
 * @param id      게시글 식별자(ID)
 * @param title   게시글 제목
 * @param content 게시글 내용
 */
public record PostResponse(
        Long id,
        String title,
        String content
) {
    // Post 엔티티 객체를 PostResponse DTO 객체로 변환
    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent());
    }
}
