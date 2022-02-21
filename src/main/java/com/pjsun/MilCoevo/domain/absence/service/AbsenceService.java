package com.pjsun.MilCoevo.domain.absence.service;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.absence.Reason;
import com.pjsun.MilCoevo.domain.absence.dto.AbsenceResponseDto;
import com.pjsun.MilCoevo.domain.absence.dto.SearchAbsenceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AbsenceService {

    Long addAbsence(Long groupId, String title, String content,
                    Reason reason, LocalDate startDate, LocalDate endDate);

    Page<AbsenceResponseDto> getAbsences(Long groupId,
                                         SearchAbsenceDto searchCondition,
                                         Pageable pageable);

    AbsenceResponseDto getAbsence(Long absenceId);

    void updateAbsence(Long groupId, Long absenceId,
                       ProcessStatus processStatus);

}
