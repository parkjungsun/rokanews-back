package com.pjsun.MilCoevo.domain.schedule.service;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.PresentStatus;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.schedule.Schedule;
import com.pjsun.MilCoevo.domain.schedule.dto.ScheduleResponseDto;
import com.pjsun.MilCoevo.domain.schedule.dto.SearchScheduleDto;
import com.pjsun.MilCoevo.domain.schedule.repository.ScheduleRepository;
import com.pjsun.MilCoevo.exception.NoPermissionException;
import com.pjsun.MilCoevo.exception.NoScheduleException;
import com.pjsun.MilCoevo.test.MockTest;
import com.pjsun.MilCoevo.test.builder.GroupBuilder;
import com.pjsun.MilCoevo.test.builder.IdentificationBuilder;
import com.pjsun.MilCoevo.test.builder.MemberBuilder;
import com.pjsun.MilCoevo.test.builder.ScheduleBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class ScheduleServiceImplTest extends MockTest {

    @InjectMocks
    ScheduleServiceImpl scheduleService;

    @Mock
    MemberService memberService;

    @Mock
    GroupRepository groupRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("그룹 스케줄 추가")
    void addScheduleTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Group group = GroupBuilder.build(1L);
        Schedule schedule = ScheduleBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(groupRepository.findById(any())).willReturn(Optional.of(group));
        given(scheduleRepository.save(any())).willReturn(schedule);

        //when
        Long id = scheduleService.addSchedule(group.getId(), schedule.getTitle(),
                schedule.getContent(), schedule.getWorkScope(), schedule.getWorkDate());
    }

    @Test
    @DisplayName("스케줄 조회")
    void getScheduleSuccessTest() {
        //given
        Schedule schedule = ScheduleBuilder.build(1L);

        given(scheduleRepository.findById(schedule.getId()))
                .willReturn(Optional.of(schedule));

        //when
        ScheduleResponseDto result = scheduleService
                .getSchedule(schedule.getId());

        //then
        assertThat(result.getId()).isEqualTo(schedule.getId());
        assertThat(result.getTitle()).isEqualTo(schedule.getTitle());
        assertThat(result.getContent()).isEqualTo(schedule.getContent());
        assertThat(result.getWorkDate()).isEqualTo(schedule.getWorkDate());
        assertThat(result.getWorkScope()).isEqualTo(schedule.getWorkScope());
        assertThat(result.getDrafterEmail()).isEqualTo(schedule.getDrafter().getEmail());
        assertThat(result.getDrafterPosition()).isEqualTo(schedule.getDrafter().getPosition());
        assertThat(result.getDrafterNickname()).isEqualTo(schedule.getDrafter().getNickname());
    }

    @Test
    @DisplayName("스케줄 조회 없음")
    void NoScheduleTest() {
        //given
        Schedule schedule = ScheduleBuilder.build(1L);

        given(scheduleRepository.findById(schedule.getId()))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> scheduleService.getSchedule(schedule.getId()))
                .isInstanceOf(NoScheduleException.class);
    }

    @Test
    @DisplayName("그룹 스케줄 조회")
    void getSchedulesTest() {
        //given
        Page<ScheduleResponseDto> schedules = new PageImpl<>(
                new ArrayList<>(10),
                PageRequest.of(0,10),
                10L);

        given(scheduleRepository.searchSchedules(any(),any(),any())).willReturn(schedules);

        //when
        Page<ScheduleResponseDto> result = scheduleService.getSchedules(1L,
                new SearchScheduleDto(
                        LocalDate.of(2022, 2, 10),
                        LocalDate.of(2022, 2, 20), null),
                PageRequest.of(0, 10));

        //then
        assertThat(result.getTotalElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("스케줄 업데이트 실패 by No Schedule")
    void updateScheduleFailTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Schedule schedule = ScheduleBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(scheduleRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> scheduleService.updateSchedule(1L, schedule.getId(), ProcessStatus.APPROVED))
                .isInstanceOf(NoScheduleException.class);
    }

    @Test
    @DisplayName("스케줄 업데이트 실패 by No Permission")
    void updateScheduleFail2Test() {
        //given
        Member member = new Member(1L, Identification.createIdentificationBuilder()
                .email("test").nickname("ni").position("po").build(), Rank.MEMBER,
                PresentStatus.MISSED, true, LocalDateTime.now(),
                null, null);
        Schedule schedule = ScheduleBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(scheduleRepository.findById(any())).willReturn(Optional.of(schedule));

        //then
        assertThatThrownBy(() -> scheduleService.updateSchedule(1L, schedule.getId(), ProcessStatus.APPROVED))
                .isInstanceOf(NoPermissionException.class);
    }

    @Test
    @DisplayName("스케줄 업데이트 성공")
    void updateScheduleSuccessTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Schedule schedule = ScheduleBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(scheduleRepository.findById(any())).willReturn(Optional.of(schedule));

        //then
        scheduleService.updateSchedule(1L, schedule.getId(), ProcessStatus.APPROVED);
    }
}