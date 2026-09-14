package com.exodusent.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 정보를 관리하는 JPA 엔티티 클래스.
 */
@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 제목과 내용을 초기화하는 빌더 패턴 생성자
    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 게시글 제목과 내용을 수정하는 비즈니스 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
