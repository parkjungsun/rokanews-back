package com.pjsun.MilCoevo.domain.schedule.dto;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchScheduleDto {

    private LocalDate frontDate;
    private LocalDate rearDate;
    private ProcessStatus processStatus;
}
