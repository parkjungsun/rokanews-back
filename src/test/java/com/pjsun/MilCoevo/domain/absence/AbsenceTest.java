package com.pjsun.MilCoevo.domain.absence;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.test.DomainTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AbsenceTest extends DomainTest {

    @Test
    @DisplayName("부재 생성")
    void createAbsenceTest() {
        //given
        String email = "email";
        String position = "position";
        String nickname = "nickname";
        String title = "title";
        String content = "content";
        Reason reason = Reason.ANNUAL;
        LocalDate startDate = LocalDate.of(2022,2,10);
        LocalDate endDate = LocalDate.of(2022,2,20);
        Identification drafter = Identification.createIdentificationBuilder()
                .email(email).position(position).nickname(nickname).build();
        
        //when
        Absence absence = Absence.createAbsenceBuilder()
                .title(title).content(content).reason(reason)
                .startDate(startDate).endDate(endDate).drafter(drafter)
                .build();
        
        //then
        assertThat(absence.getTitle()).isEqualTo(title);
        assertThat(absence.getContent()).isEqualTo(content);
        assertThat(absence.getReason()).isEqualTo(reason);
        assertThat(absence.getStartDate()).isEqualTo(startDate);
        assertThat(absence.getEndDate()).isEqualTo(endDate);
        assertThat(absence.getDrafter()).isEqualTo(drafter);
    }
}