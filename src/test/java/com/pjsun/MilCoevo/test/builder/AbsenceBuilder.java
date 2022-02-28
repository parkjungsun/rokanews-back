package com.pjsun.MilCoevo.test.builder;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.absence.Absence;
import com.pjsun.MilCoevo.domain.absence.Reason;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.test.builder.IdentificationBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AbsenceBuilder {

    public static Absence build(Long id) {
        return new Absence(id, "title", "content", Reason.ETC_REASON,
                ProcessStatus.SUGGESTED, LocalDate.now(), LocalDate.now(), null,
                IdentificationBuilder.build(), null, null);
    }
}
