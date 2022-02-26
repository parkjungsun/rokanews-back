package com.pjsun.MilCoevo.test.builder;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.schedule.Schedule;
import com.pjsun.MilCoevo.domain.schedule.WorkScope;

import java.time.LocalDateTime;

public class ScheduleBuilder {

    public static Schedule build(Long id) {
        return new Schedule(id, "schedule", "content",
                LocalDateTime.now(), WorkScope.ALL_DAY, ProcessStatus.SUGGESTED,
                IdentificationBuilder.build(), null, null, null);
    }
}
