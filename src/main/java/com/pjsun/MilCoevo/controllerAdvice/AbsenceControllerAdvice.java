package com.pjsun.MilCoevo.controllerAdvice;


import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.NoAbsenceException;
import com.querydsl.core.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AbsenceControllerAdvice {

    @Value("${error.absence.noAbsenceException}")
    private String NO_ABSENCE_EXCEPTION;

    @ExceptionHandler(NoAbsenceException.class)
    public ResponseEntity<ResponseDto> noAbsenceEx(NoAbsenceException e) {

        log.debug("Exception: noAbsenceEx");
        ResponseDto error = new ResponseDto(NO_ABSENCE_EXCEPTION,e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
