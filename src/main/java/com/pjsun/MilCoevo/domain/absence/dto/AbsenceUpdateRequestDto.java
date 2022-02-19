package com.pjsun.MilCoevo.domain.absence.dto;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AbsenceUpdateRequestDto {

    @NotNull
    private ProcessStatus processStatus;
}
