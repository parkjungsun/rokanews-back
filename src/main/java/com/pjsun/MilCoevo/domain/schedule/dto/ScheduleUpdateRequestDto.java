package com.pjsun.MilCoevo.domain.schedule.dto;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleUpdateRequestDto {

    @NotNull
    private ProcessStatus processStatus;
}
