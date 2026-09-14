package com.exodusent.controller;

import com.exodusent.dto.VoteCreateRequest;
import com.exodusent.dto.VoteResponse;
import com.exodusent.dto.VoteResultResponse;
import com.exodusent.exception.BusinessException;
import com.exodusent.exception.GlobalExceptionHandler;
import com.exodusent.exception.errorcode.VoteErrorCode;
import com.exodusent.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VoteService voteService;

    @InjectMocks
    private VoteController voteController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(voteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("정상적인 투표 요청 시 201 Created와 투표 정보를 반환한다.")
    void vote_Success() throws Exception {
        // given
        VoteResponse response = new VoteResponse(1L, "jajang", "user-123");
        given(voteService.vote(any(VoteCreateRequest.class))).willReturn(response);

        String requestJson = """
                {
                  "choice": "jajang",
                  "voterId": "user-123"
                }
                """;

        // when & then
        mockMvc.perform(post("/api/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.choice").value("jajang"))
                .andExpect(jsonPath("$.voterId").value("user-123"));
    }

    @Test
    @DisplayName("이미 투표한 voterId로 요청 시 409 Conflict를 반환한다.")
    void vote_DuplicateVoterId_Returns409() throws Exception {
        // given
        given(voteService.vote(any(VoteCreateRequest.class)))
                .willThrow(new BusinessException(VoteErrorCode.ALREADY_VOTED));

        String requestJson = """
                {
                  "choice": "jjamppong",
                  "voterId": "user-123"
                }
                """;

        // when & then
        mockMvc.perform(post("/api/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("V001"));
    }

    @Test
    @DisplayName("잘못된 choice 요청 시 400 Bad Request를 반환한다.")
    void vote_InvalidChoice_Returns400() throws Exception {
        String requestJson = """
                {
                  "choice": "tangsooyook",
                  "voterId": "user-123"
                }
                """;

        mockMvc.perform(post("/api/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("voterId 누락 시 400 Bad Request를 반환한다.")
    void vote_MissingVoterId_Returns400() throws Exception {
        String requestJson = """
                {
                  "choice": "jajang",
                  "voterId": ""
                }
                """;

        mockMvc.perform(post("/api/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("현재 투표 결과를 조회하면 200 OK와 집계 결과를 반환한다.")
    void getResult_Success() throws Exception {
        // given
        VoteResultResponse response = VoteResultResponse.of(120, 95);
        given(voteService.getVoteResult()).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/result"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jajang").value(120))
                .andExpect(jsonPath("$.jjamppong").value(95))
                .andExpect(jsonPath("$.total").value(215));
    }
}
