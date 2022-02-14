package com.pjsun.MilCoevo.domain.group.controller;

import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.InactiveGroupException;
import com.pjsun.MilCoevo.exception.InactiveUserException;
import com.pjsun.MilCoevo.exception.NoExistGroupException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GroupControllerAdvice {

    @Value("${error.group.noExistGroupException}")
    private String NO_EXIST_GROUP_EXCEPTION;

    @Value("${error.group.inactiveGroupException}")
    private String INACTIVE_GROUP_EXCEPTION;

    @ExceptionHandler(InactiveGroupException.class)
    public ResponseEntity<ResponseDto> inactiveGroupEx(InactiveGroupException e) {

        log.debug("Exception: inactiveGroupEx");
        ResponseDto message = new ResponseDto(INACTIVE_GROUP_EXCEPTION, "No Active Group");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ExceptionHandler(NoExistGroupException.class)
    public ResponseEntity<ResponseDto> noExistGroupEx(NoExistGroupException e) {

        log.debug("Exception: noExistGroupEx");
        ResponseDto message = new ResponseDto(NO_EXIST_GROUP_EXCEPTION, "No Exist Group");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
