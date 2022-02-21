package com.pjsun.MilCoevo.domain.schedule.service;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.schedule.WorkScope;
import com.pjsun.MilCoevo.domain.schedule.dto.ScheduleResponseDto;
import com.pjsun.MilCoevo.domain.schedule.dto.SearchScheduleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ScheduleService {

    Page<ScheduleResponseDto> getSchedules(
            Long groupId, SearchScheduleDto searchCondition, Pageable pageable);

    ScheduleResponseDto getSchedule(Long scheduleId);

    Long addSchedule(Long groupId, String title, String content,
                            WorkScope workScope, LocalDateTime workDate);

    void updateSchedule(Long groupId, Long scheduleId,
                               ProcessStatus processStatus);

}
