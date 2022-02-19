package com.pjsun.MilCoevo.domain.absence.repository;

import com.pjsun.MilCoevo.domain.absence.dto.AbsenceResponseDto;
import com.pjsun.MilCoevo.domain.absence.dto.SearchAbsenceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AbsenceRepositoryCustom {

    Page<AbsenceResponseDto> searchAbsences(Long groupId, SearchAbsenceDto searchCondition, Pageable pageable);

}
