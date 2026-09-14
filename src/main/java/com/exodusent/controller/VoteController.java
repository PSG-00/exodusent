package com.exodusent.controller;

import com.exodusent.dto.VoteCreateRequest;
import com.exodusent.dto.VoteResponse;
import com.exodusent.dto.VoteResultResponse;
import com.exodusent.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 투표 및 결과 조회를 처리하는 컨트롤러.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    /**
     * 투표하기 API
     * POST /api/vote
     *
     * @param request 투표 생성 요청 DTO
     * @return 생성된 투표 정보 응답
     */
    @PostMapping("/vote")
    public ResponseEntity<VoteResponse> vote(@Valid @RequestBody VoteCreateRequest request) {
        VoteResponse response = voteService.vote(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 현재 투표 결과 조회 API
     * GET /api/result
     *
     * @return 현재 투표 집계 결과 응답 (jajang, jjamppong, total)
     */
    @GetMapping("/result")
    public ResponseEntity<VoteResultResponse> getResult() {
        VoteResultResponse response = voteService.getVoteResult();
        return ResponseEntity.ok(response);
    }
}
