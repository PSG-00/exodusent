package com.exodusent.controller;

import com.exodusent.dto.PostCreateRequest;
import com.exodusent.dto.PostResponse;
import com.exodusent.dto.PostUpdateRequest;
import com.exodusent.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시글(Post) 관련 REST API 요청을 처리하는 프레젠테이션 계층 컨트롤러 클래스.
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 등록 요청(POST)을 수신하고 201 Created 응답 반환
    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody PostCreateRequest request) {
        PostResponse response = postService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 등록된 전체 게시글 조회 요청(GET)을 수신하고 200 OK 응답 반환
    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    // ID 기반 특정 게시글 단건 조회 요청(GET)을 수신하고 200 OK 응답 반환
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    // ID 기반 게시글 수정 요청(PUT)을 수신하고 수정된 결과와 200 OK 응답 반환
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long id,
            @RequestBody PostUpdateRequest request
    ) {
        return ResponseEntity.ok(postService.update(id, request));
    }

    // ID 기반 게시글 삭제 요청(DELETE)을 수신하고 204 No Content 응답 반환
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
