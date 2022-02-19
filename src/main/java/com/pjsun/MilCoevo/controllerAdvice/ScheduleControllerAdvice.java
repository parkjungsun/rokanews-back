package com.pjsun.MilCoevo.controllerAdvice;

import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.NoMemberException;
import com.pjsun.MilCoevo.exception.NoScheduleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ScheduleControllerAdvice {

    @Value("${error.schedule.noScheduleException}")
    private String NO_SCHEDULE_EXCEPTION;

    @ExceptionHandler(NoScheduleException.class)
    public ResponseEntity<ResponseDto> noScheduleEx(NoScheduleException e) {

        log.debug("Exception: noScheduleEx");
        ResponseDto message = new ResponseDto(NO_SCHEDULE_EXCEPTION, e.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
