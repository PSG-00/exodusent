package com.exodusent.repository;

import com.exodusent.entity.Vote;
import com.exodusent.entity.VoteChoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 투표 데이터 접근을 제공하는 JPA 리포지토리.
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByVoterId(String voterId);

    long countByChoice(VoteChoice choice);
}
