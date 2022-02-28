package com.pjsun.MilCoevo.domain.absence.service;

import com.pjsun.MilCoevo.domain.absence.Absence;
import com.pjsun.MilCoevo.domain.absence.dto.AbsenceResponseDto;
import com.pjsun.MilCoevo.domain.absence.dto.SearchAbsenceDto;
import com.pjsun.MilCoevo.domain.absence.repository.AbsenceRepository;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.PresentStatus;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.exception.NoAbsenceException;
import com.pjsun.MilCoevo.exception.NoPermissionException;
import com.pjsun.MilCoevo.test.MockTest;
import com.pjsun.MilCoevo.test.builder.AbsenceBuilder;
import com.pjsun.MilCoevo.test.builder.GroupBuilder;
import com.pjsun.MilCoevo.test.builder.MemberBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

class AbsenceServiceImplTest extends MockTest {

    @InjectMocks
    AbsenceServiceImpl absenceService;

    @Mock
    MemberService memberService;

    @Mock
    GroupRepository groupRepository;

    @Mock
    AbsenceRepository absenceRepository;

    @Test
    @DisplayName("그룹 부재 추가")
    void addAbsenceTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Group group = GroupBuilder.build(1L);
        Absence absence = AbsenceBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(groupRepository.findById(any())).willReturn(Optional.of(group));
        given(absenceRepository.save(any())).willReturn(absence);

        //then
        absenceService.addAbsence(group.getId(), absence.getTitle(), absence.getContent(),
                absence.getReason(), absence.getStartDate(), absence.getEndDate());
    }

    @Test
    @DisplayName("부재 가져오기")
    void getAbsenceTest() {
        //given
        Absence absence = AbsenceBuilder.build(1L);

        given(absenceRepository.findById(any())).willReturn(Optional.of(absence));

        //when
        AbsenceResponseDto result = absenceService.getAbsence(absence.getId());

        //then
        assertThat(result.getId()).isEqualTo(absence.getId());
        assertThat(result.getTitle()).isEqualTo(absence.getTitle());
        assertThat(result.getContent()).isEqualTo(absence.getContent());
        assertThat(result.getStartDate()).isEqualTo(absence.getStartDate());
        assertThat(result.getEndDate()).isEqualTo(absence.getEndDate());
        assertThat(result.getReason()).isEqualTo(absence.getReason());
        assertThat(result.getDrafterEmail()).isEqualTo(absence.getDrafter().getEmail());
        assertThat(result.getDrafterPosition()).isEqualTo(absence.getDrafter().getPosition());
        assertThat(result.getDrafterNickname()).isEqualTo(absence.getDrafter().getNickname());
    }

    @Test
    @DisplayName("부재 가져오기 실패")
    void getAbsenceFailTest() {
        //given
        Absence absence = AbsenceBuilder.build(1L);

        given(absenceRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> absenceService.getAbsence(absence.getId()))
                .isInstanceOf(NoAbsenceException.class);
    }

    @Test
    @DisplayName("그룹 부재 가져오기")
    void getAbsencesTest() {
        //given
        Page<AbsenceResponseDto> absences = new PageImpl<>(
                new ArrayList<>(10),
                PageRequest.of(0,10),
                10L);

        given(absenceRepository.searchAbsences(any(),any(),any())).willReturn(absences);

        //when
        Page<AbsenceResponseDto> result = absenceService
                .getAbsences(1L,
                        new SearchAbsenceDto(
                                LocalDate.of(2022, 2, 10),
                                LocalDate.of(2022, 2, 20), null),
                        PageRequest.of(0, 10));

        //then
        assertThat(result.getTotalElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("부재 업데이트")
    void updateAbsenceTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Absence absence = AbsenceBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(absenceRepository.findById(any())).willReturn(Optional.of(absence));

        //then
        absenceService.updateAbsence(1L, absence.getId(), absence.getProcessStatus());
    }

    @Test
    @DisplayName("부재 업데이트 실패 존재하지 않는 부재")
    void updateAbsenceFailTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Absence absence = AbsenceBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(absenceRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> absenceService.updateAbsence(1L, absence.getId(), absence.getProcessStatus()))
                .isInstanceOf(NoAbsenceException.class);
    }

    @Test
    @DisplayName("부재 업데이트 실패 권한 부족")
    void updateAbsenceFail2Test() {
        //given
        Member member = new Member(1L, Identification.createIdentificationBuilder()
                .email("test").nickname("ni").position("po").build(), Rank.MEMBER,
                PresentStatus.MISSED, true, LocalDateTime.now(),
                null, null);
        Absence absence = AbsenceBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(absenceRepository.findById(any())).willReturn(Optional.of(absence));

        //then
        assertThatThrownBy(() -> absenceService.updateAbsence(1L, absence.getId(), absence.getProcessStatus()))
                .isInstanceOf(NoPermissionException.class);
    }
}