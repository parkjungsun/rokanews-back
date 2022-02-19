package com.pjsun.MilCoevo.domain.absence.dto;

import com.pjsun.MilCoevo.domain.absence.Reason;
import com.pjsun.MilCoevo.domain.schedule.WorkScope;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AbsenceAddRequestDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @Size(max = 4096)
    private String content;

    @NotNull
    private Reason reason;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
