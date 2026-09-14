package com.exodusent.dto;

import com.exodusent.entity.Vote;

/**
 * 투표 응답 DTO.
 */
public record VoteResponse(
        Long id,
        String choice,
        String voterId
) {

    public static VoteResponse from(Vote vote) {
        return new VoteResponse(
                vote.getId(),
                vote.getChoice().getValue(),
                vote.getVoterId()
        );
    }
}
