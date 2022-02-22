package com.pjsun.MilCoevo.domain.absence.controller;

import com.pjsun.MilCoevo.domain.absence.dto.AbsenceAddRequestDto;
import com.pjsun.MilCoevo.domain.absence.dto.AbsenceResponseDto;
import com.pjsun.MilCoevo.domain.absence.dto.AbsenceUpdateRequestDto;
import com.pjsun.MilCoevo.domain.absence.dto.SearchAbsenceDto;
import com.pjsun.MilCoevo.domain.absence.service.AbsenceService;
import com.pjsun.MilCoevo.domain.absence.service.AbsenceServiceImpl;
import com.pjsun.MilCoevo.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/absence")
public class AbsenceController {

    private final AbsenceService absenceService;

    @Value("${error.common.bindingError}")
    private String BINDING_ERROR_EXCEPTION;

    @Value("${success.common.response}")
    private String SUCCESS_RESPONSE;

    @PostMapping("/{groupId}")
    public ResponseEntity<ResponseDto> addAbsence(
            @PathVariable Long groupId,
            @Validated @RequestBody AbsenceAddRequestDto requestDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Long absenceId = absenceService.addAbsence(groupId, requestDto.getTitle(), requestDto.getContent(),
                requestDto.getReason(), requestDto.getStartDate(), requestDto.getEndDate());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, absenceId);

        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}/detail/{absenceId}")
    public ResponseEntity<ResponseDto> getAbsence(@PathVariable Long absenceId) {

        AbsenceResponseDto absence = absenceService.getAbsence(absenceId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, absence);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseDto> getAbsences(
            @PathVariable Long groupId,
            @ModelAttribute SearchAbsenceDto searchCondition,
            Pageable pageable) {

        Page<AbsenceResponseDto> absences = absenceService
                .getAbsences(groupId, searchCondition, pageable);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, absences);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PatchMapping("/{groupId}/detail/{absenceId}")
    public ResponseEntity<ResponseDto> updateAbsence(
            @PathVariable Long groupId,
            @PathVariable Long absenceId,
            @Validated @RequestBody AbsenceUpdateRequestDto responseDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        absenceService.updateAbsence(groupId, absenceId, responseDto.getProcessStatus());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
