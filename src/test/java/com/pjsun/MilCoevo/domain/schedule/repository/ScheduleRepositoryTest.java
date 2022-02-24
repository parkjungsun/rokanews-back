package com.pjsun.MilCoevo.domain.schedule.repository;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.domain.schedule.Schedule;
import com.pjsun.MilCoevo.domain.schedule.WorkScope;
import com.pjsun.MilCoevo.domain.schedule.dto.ScheduleResponseDto;
import com.pjsun.MilCoevo.domain.schedule.dto.SearchScheduleDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    GroupRepository groupRepository;

    Group group;

    @BeforeEach
    void init()  {
        group = Group.createGroupBuilder("testGroup");
        groupRepository.save(group);

        Identification sign = Identification.createIdentificationBuilder()
                        .email("schedule@test.com").nickname("schedule")
                        .position("scheduler").build();
        for(int i = 1; i < 20; i++) {
            Schedule schedule = Schedule.createScheduleBuilder()
                    .title("test").content("content")
                    .workScope(WorkScope.ALL_DAY)
                    .workDate(LocalDateTime
                            .of(2022, 2, i, 14, 0))
                    .drafter(sign).group(group).build();
            scheduleRepository.save(schedule);
        }
    }

    @Test
    @DisplayName("스케줄 리스트 불러오기")
    void searchScheduleTest() {
        //given
        SearchScheduleDto searchCondition = new SearchScheduleDto(
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022,2,15),
                ProcessStatus.SUGGESTED);

        Pageable pageable = PageRequest.of(0, 8);

        //when
        Page<ScheduleResponseDto> response = scheduleRepository
                .searchSchedules(group.getId(), searchCondition, pageable);

        //then
        assertThat(response.getContent().size()).isEqualTo(5);
        assertThat(response.getSize()).isEqualTo(8);
    }
}