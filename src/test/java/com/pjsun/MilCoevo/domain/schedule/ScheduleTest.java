package com.pjsun.MilCoevo.domain.schedule;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.test.DomainTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ScheduleTest extends DomainTest {

    Schedule schedule;
    String title;
    String content;
    WorkScope workscope;
    LocalDateTime workDate;
    Identification drafter;

    @BeforeEach
    void init() {
        //given
        title = "title";
        content = "content";
        workscope = WorkScope.ALL_DAY;
        workDate = LocalDateTime.of(2022, 2, 24, 14, 0);
        drafter = Identification.createIdentificationBuilder()
                .email("test@test.com").position("position").nickname("nickname").build();

        //when
        schedule = Schedule.createScheduleBuilder()
                .title(title).content(content).workScope(workscope)
                .workDate(workDate).drafter(drafter).build();
    }


    @Test
    @DisplayName("스케줄 생성")
    void createScheduleTest() {
        //then
        assertThat(schedule.getTitle()).isEqualTo(title);
        assertThat(schedule.getContent()).isEqualTo(content);
        assertThat(schedule.getWorkDate()).isEqualTo(workDate);
        assertThat(schedule.getWorkScope()).isEqualTo(workscope);
        assertThat(schedule.getDrafter()).isEqualTo(drafter);
    }

    @Test
    @DisplayName("비즈니스 로직")
    void decideTest() {
        //when
        schedule.decide(drafter, ProcessStatus.APPROVED);

        //then
        assertThat(schedule.getProcessStatus()).isEqualTo(ProcessStatus.APPROVED);
        assertThat(schedule.getArbiter()).isEqualTo(drafter);
    }
}