package com.exodusent.repository;

import com.exodusent.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Post 엔티티에 대한 데이터베이스 접근 및 CRUD 연산을 제공하는 JPA 리포지토리 인터페이스.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
}
