package com.minecraft.job.api.controller;

import com.minecraft.job.api.fixture.RecruitmentFixture;
import com.minecraft.job.api.fixture.TeamFixture;
import com.minecraft.job.api.fixture.UserFixture;
import com.minecraft.job.api.support.ApiTest;
import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.recruitment.domain.RecruitmentRepository;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.team.domain.TeamRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.minecraft.job.api.controller.dto.RecruitmentProcessCreateDto.RecruitmentProcessCreateRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecruitmentProcessApiTest extends ApiTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private RecruitmentRepository recruitmentRepository;

    private User user;
    private Recruitment recruitment;


    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
        User leader = userRepository.save(UserFixture.getAntherUser("leader"));
        Team team = teamRepository.save(TeamFixture.create(leader));
        recruitment = recruitmentRepository.save(RecruitmentFixture.create(team));
    }

    @Test
    void 채용과정_생성_성공() throws Exception {
        RecruitmentProcessCreateRequest recruitmentProcessCreateRequest = new RecruitmentProcessCreateRequest(recruitment.getId(), user.getId());

        mockMvc.perform(post("/recruitmentProcess")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recruitmentProcessCreateRequest)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.recruitmentProcess.id").isNotEmpty()
                );
    }
}
