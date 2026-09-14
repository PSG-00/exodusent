package com.exodusent.service;

import com.exodusent.dto.VoteCreateRequest;
import com.exodusent.dto.VoteResponse;
import com.exodusent.dto.VoteResultResponse;
import com.exodusent.entity.Vote;
import com.exodusent.entity.VoteChoice;
import com.exodusent.exception.BusinessException;
import com.exodusent.exception.errorcode.VoteErrorCode;
import com.exodusent.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 투표 비즈니스 로직 및 트랜잭션 관리 서비스.
 */
@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;

    /**
     * 투표 처리 메서드.
     * 동일 voterId에 대한 중복 투표 방지 및 동시성 처리를 위해 DB Unique 제약조건 위반 예외를 처리합니다.
     */
    @Transactional
    public VoteResponse vote(VoteCreateRequest request) {
        if (voteRepository.existsByVoterId(request.voterId())) {
            throw new BusinessException(VoteErrorCode.ALREADY_VOTED);
        }

        Vote vote = Vote.builder()
                .voterId(request.voterId())
                .choice(request.toVoteChoice())
                .build();

        try {
            Vote savedVote = voteRepository.saveAndFlush(vote);
            return VoteResponse.from(savedVote);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(VoteErrorCode.ALREADY_VOTED);
        }
    }

    /**
     * 현재 투표 집계 결과 조회 메서드.
     */
    @Transactional(readOnly = true)
    public VoteResultResponse getVoteResult() {
        long jajangCount = voteRepository.countByChoice(VoteChoice.JAJANG);
        long jjamppongCount = voteRepository.countByChoice(VoteChoice.JJAMPPONG);

        return VoteResultResponse.of(jajangCount, jjamppongCount);
    }
}
