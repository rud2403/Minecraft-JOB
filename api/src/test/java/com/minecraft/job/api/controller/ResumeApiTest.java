package com.minecraft.job.api.controller;

import com.minecraft.job.api.controller.dto.ResumeActivateDto.ResumeActivateRequest;
import com.minecraft.job.api.controller.dto.ResumeDeleteDto.ResumeDeleteRequest;
import com.minecraft.job.api.controller.dto.ResumeInactivateDto.ResumeInactivateRequest;
import com.minecraft.job.api.controller.dto.ResumeUpdateDto.ResumeUpdateRequest;
import com.minecraft.job.api.fixture.ResumeFixture;
import com.minecraft.job.api.fixture.UserFixture;
import com.minecraft.job.api.support.ApiTest;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.minecraft.job.api.controller.dto.ResumeCreateDto.ResumeCreateRequest;
import static com.minecraft.job.common.resume.domain.ResumeStatue.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResumeApiTest extends ApiTest {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Resume resume;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
        resume = resumeRepository.save(ResumeFixture.create(user));
    }

    @Test
    void 이력서_생성_성공() throws Exception {
        ResumeCreateRequest req = new ResumeCreateRequest(user.getId(), "title", "content", "trainingHistory");

        mockMvc.perform(post("/resume")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.resume.id").isNotEmpty(),
                        jsonPath("$.resume.title").value("title")
                );

        Resume findResume = resumeRepository.findAll().get(0);

        assertThat(findResume.getId()).isNotNull();
        assertThat(findResume.getTitle()).isEqualTo("title");
        assertThat(findResume.getContent()).isEqualTo("content");
        assertThat(findResume.getTrainingHistory()).isEqualTo("trainingHistory");
        assertThat(findResume.getUser()).isEqualTo(user);
        assertThat(findResume.getStatus()).isEqualTo(CREATED);
        assertThat(findResume.getCreatedAt()).isNotNull();
    }

    @Test
    void 이력서_수정_성공() throws Exception {
        ResumeUpdateRequest req = new ResumeUpdateRequest(resume.getId(), user.getId(), "updateTitle", "updateContent", "updateTrainingHistory");

        mockMvc.perform(post("/resume/update")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpectAll(
                status().isOk()
        );

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getTitle()).isEqualTo("updateTitle");
        assertThat(findResume.getContent()).isEqualTo("updateContent");
        assertThat(findResume.getTrainingHistory()).isEqualTo("updateTrainingHistory");
    }

    @Test
    void 이력서_활성화_성공() throws Exception {
        ResumeActivateRequest req = new ResumeActivateRequest(resume.getId(), user.getId());

        mockMvc.perform(post("/resume/activate")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(status().isOk());

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getStatus()).isEqualTo(ACTIVATED);
    }

    @Test
    void 이력서_비활성화_성공() throws Exception {
        ResumeInactivateRequest req = new ResumeInactivateRequest(resume.getId(), user.getId());

        mockMvc.perform(post("/resume/inactivate")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(status().isOk());

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getStatus()).isEqualTo(INACTIVATED);
    }

    @Test
    void 이력서_삭제_성공() throws Exception {
        ResumeDeleteRequest req = new ResumeDeleteRequest(resume.getId(), user.getId());

        mockMvc.perform(post("/resume/delete")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(status().isOk());

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getStatus()).isEqualTo(DELETED);
    }
}
