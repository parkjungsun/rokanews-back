package com.pjsun.MilCoevo.domain.schedule.repository;

import com.pjsun.MilCoevo.domain.schedule.dto.ScheduleResponseDto;
import com.pjsun.MilCoevo.domain.schedule.dto.SearchScheduleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleRepositoryCustom {

    Page<ScheduleResponseDto> searchSchedules(Long groupId, SearchScheduleDto searchCondition, Pageable pageable);

}
