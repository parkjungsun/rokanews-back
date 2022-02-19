package com.pjsun.MilCoevo.domain.schedule.dto;

import com.pjsun.MilCoevo.domain.schedule.WorkScope;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ScheduleAddRequestDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @Size(max = 4096)
    private String content;

    @NotNull
    private WorkScope workScope;

    @NotNull
    private LocalDateTime workDate;

}
