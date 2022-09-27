package com.minecraft.job.common.recruitment.domain;

import com.minecraft.job.common.fixture.TeamFixture;
import com.minecraft.job.common.team.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.minecraft.job.common.recruitment.domain.RecruitmentStatus.CREATED;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class RecruitmentRepositoryTest {

    private Team team;

    @BeforeEach
    void setUp() { team = TeamFixture.create(); }

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Test
    void 채용공고_생성_성공() {
        Recruitment recruitment = Recruitment.create("title", "content", team);

        recruitment = recruitmentRepository.save(recruitment);

        Recruitment findRecruitment = recruitmentRepository.findById(recruitment.getId()).orElseThrow();

        assertThat(findRecruitment.getId()).isNotNull();
        assertThat(findRecruitment.getTitle()).isEqualTo("title");
        assertThat(findRecruitment.getContent()).isEqualTo("content");
        assertThat(findRecruitment.getTeam()).isNotNull();
        assertThat(findRecruitment.getStatus()).isEqualTo(CREATED);
        assertThat(findRecruitment.getCreatedAt()).isNotNull();
    }
}
