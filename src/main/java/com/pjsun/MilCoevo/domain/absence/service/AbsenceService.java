package com.pjsun.MilCoevo.domain.absence.service;

import com.pjsun.MilCoevo.domain.absence.repository.AbsenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbsenceService {

    private final AbsenceRepository absenceRepository;

}
