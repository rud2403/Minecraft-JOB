package com.minecraft.job.common.resume.service;

import com.minecraft.job.common.fixture.ResumeFixture;
import com.minecraft.job.common.fixture.UserFixture;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.resume.domain.ResumeStatue.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
@Transactional
class DomainResumeServiceTest {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    private Resume resume;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.create());
        resume = resumeRepository.save(ResumeFixture.create(user));
    }

    @Test
    void 이력서_생성_성공() {
        Resume resume = resumeService.create(user.getId(), "title", "content", "trainingHistory");

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getId()).isNotNull();
    }

    @Test
    void 이력서_수정_성공() {
        resumeService.update(resume.getId(), user.getId(), "updateTitle", "updateContent", "updateTrainingHistory");

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getTitle()).isEqualTo("updateTitle");
        assertThat(findResume.getContent()).isEqualTo("updateContent");
        assertThat(findResume.getTrainingHistory()).isEqualTo("updateTrainingHistory");
    }

    @Test
    void 이력서_수정_실패__유저의_이력서가_아님() {
        User fakeUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(() -> resumeService.update(resume.getId(), fakeUser.getId(), "updateTitle", "updateContent", "updateTrainingHistory"));
    }

    @Test
    void 이력서_활성화_성공() {
        resumeService.activate(resume.getId(), user.getId());

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getStatus()).isEqualTo(ACTIVATED);
    }

    @Test
    void 이력서_활성화_실패__유저의_이력서가_아님() {
        User fakeUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(() -> resumeService.activate(resume.getId(), fakeUser.getId()));
    }

    @Test
    void 이력서_비활성화_성공() {
        resumeService.inactivate(resume.getId(), user.getId());

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getStatus()).isEqualTo(INACTIVATED);
    }

    @Test
    void 이력서_비활성화_실패__유저의_이력서가_아님() {
        User fakeUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(() -> resumeService.inactivate(resume.getId(), fakeUser.getId()));
    }

    @Test
    void 이력서_삭제_성공() {
        resumeService.delete(resume.getId(), user.getId());

        Resume findResume = resumeRepository.findById(resume.getId()).orElseThrow();

        assertThat(findResume.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 이력서_삭제_실패__유저의_이력서가_아님() {
        User fakeUser = userRepository.save(UserFixture.getFakerUser());

        assertThatIllegalArgumentException().isThrownBy(() -> resumeService.delete(resume.getId(), fakeUser.getId()));
    }
}
