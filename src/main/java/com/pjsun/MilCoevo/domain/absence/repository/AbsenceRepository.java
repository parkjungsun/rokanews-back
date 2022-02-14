package com.pjsun.MilCoevo.domain.absence.repository;

import com.pjsun.MilCoevo.domain.absence.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsenceRepository extends JpaRepository<Absence, Long>, AbsenceRepositoryCustom {
}
