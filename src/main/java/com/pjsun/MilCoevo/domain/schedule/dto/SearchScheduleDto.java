package com.pjsun.MilCoevo.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pjsun.MilCoevo.domain.ProcessStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchScheduleDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate frontDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rearDate;

    private ProcessStatus processStatus;
}
