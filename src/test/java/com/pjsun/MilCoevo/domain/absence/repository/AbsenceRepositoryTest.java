package com.pjsun.MilCoevo.domain.absence.repository;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.absence.Absence;
import com.pjsun.MilCoevo.domain.absence.Reason;
import com.pjsun.MilCoevo.domain.absence.dto.AbsenceResponseDto;
import com.pjsun.MilCoevo.domain.absence.dto.SearchAbsenceDto;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Identification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AbsenceRepositoryTest {

    @Autowired
    AbsenceRepository absenceRepository;

    @Autowired
    GroupRepository groupRepository;

    Group group;

    @BeforeEach
    void init() {
        group = Group.createGroupBuilder("testGroup");
        groupRepository.save(group);

        Identification sign = Identification.createIdentificationBuilder()
                .email("schedule@test.com").nickname("schedule")
                .position("scheduler").build();

        for(int i = 1; i < 20; i++) {
            Absence absence = Absence.createAbsenceBuilder()
                    .title("test").content("content")
                    .reason(Reason.ANNUAL)
                    .startDate(LocalDate.of(2022, 2, i))
                    .endDate(LocalDate.of(2022, 2, i))
                    .drafter(sign).group(group).build();
            absenceRepository.save(absence);
        }
    }

    @Test
    @DisplayName("부재 리스트")
    void searchAbsencesTest() {
        //given
        SearchAbsenceDto searchCondition = new SearchAbsenceDto(
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022,2,15),
                ProcessStatus.SUGGESTED);

        Pageable pageable = PageRequest.of(0,10);

        //when
        Page<AbsenceResponseDto> response = absenceRepository
                .searchAbsences(group.getId(), searchCondition, pageable);

        //then
        assertThat(response.getContent().size()).isEqualTo(6);
        assertThat(response.getSize()).isEqualTo(10);
    }
}