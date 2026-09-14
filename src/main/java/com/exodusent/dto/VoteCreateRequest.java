package com.exodusent.dto;

import com.exodusent.entity.VoteChoice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 투표 생성 요청 DTO.
 */
public record VoteCreateRequest(
        @NotBlank(message = "choice는 필수입니다.")
        @Pattern(regexp = "jajang|jjamppong", message = "choice는 jajang 또는 jjamppong이어야 합니다.")
        String choice,

        @NotBlank(message = "voterId는 필수입니다.")
        String voterId
) {

    public VoteChoice toVoteChoice() {
        return VoteChoice.fromValue(choice);
    }
}
