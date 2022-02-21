package com.pjsun.MilCoevo.domain.absence.dto;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchAbsenceDto {

    private LocalDate frontDate;
    private LocalDate rearDate;
    private ProcessStatus processStatus;
}
