package com.pjsun.MilCoevo.domain.schedule.repository;

import com.pjsun.MilCoevo.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {
}
