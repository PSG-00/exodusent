package com.exodusent.service;

import com.exodusent.dto.PostCreateRequest;
import com.exodusent.dto.PostResponse;
import com.exodusent.dto.PostUpdateRequest;
import com.exodusent.entity.Post;
import com.exodusent.exception.BusinessException;
import com.exodusent.exception.errorcode.CommonErrorCode;
import com.exodusent.exception.errorcode.PostErrorCode;
import com.exodusent.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시글(Post) 도메인의 비즈니스 로직 및 트랜잭션 처리를 담당하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    // 새로운 게시글을 등록하고 저장된 게시글 정보를 반환
    @Transactional
    public PostResponse create(PostCreateRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new BusinessException(CommonErrorCode.INVALID_INPUT_VALUE, "field", "title");
        }

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .build();

        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }

    // 등록된 전체 게시글 목록을 조회하여 DTO 리스트로 반환
    public List<PostResponse> findAll() {
        return postRepository.findAll().stream()
                .map(PostResponse::from)
                .toList();
    }

    // ID로 게시글을 단건 조회하며, 존재하지 않을 경우 요청된 postId를 details에 담아 BusinessException 발생
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PostErrorCode.POST_NOT_FOUND, "postId", id));
        return PostResponse.from(post);
    }

    // ID로 기존 게시글을 조회하여 제목 및 내용을 수정
    @Transactional
    public PostResponse update(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PostErrorCode.POST_NOT_FOUND, "postId", id));

        post.update(request.title(), request.content());
        return PostResponse.from(post);
    }

    // ID에 해당하는 게시글의 존재 여부를 확인한 뒤 삭제 수행
    @Transactional
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new BusinessException(PostErrorCode.POST_NOT_FOUND, "postId", id);
        }
        postRepository.deleteById(id);
    }
}
