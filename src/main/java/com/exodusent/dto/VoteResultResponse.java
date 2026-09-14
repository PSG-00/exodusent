package com.exodusent.dto;

/**
 * 투표 집계 결과 응답 DTO.
 */
public record VoteResultResponse(
        long jajang,
        long jjamppong,
        long total
) {
    public static VoteResultResponse of(long jajang, long jjamppong) {
        return new VoteResultResponse(jajang, jjamppong, jajang + jjamppong);
    }
}
