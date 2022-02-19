package com.pjsun.MilCoevo.domain.absence.dto;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchAbsenceDto {

    private LocalDate frontDate;
    private LocalDate rearDate;
    private ProcessStatus processStatus;
}
