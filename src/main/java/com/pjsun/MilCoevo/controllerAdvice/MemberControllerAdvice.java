package com.pjsun.MilCoevo.controllerAdvice;

import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.InactiveUserException;
import com.pjsun.MilCoevo.exception.MaxMemberException;
import com.pjsun.MilCoevo.exception.NoMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MemberControllerAdvice {

    @Value("${error.member.maxMemberException}")
    private String MAX_MEMBER_EXCEPTION;

    @Value("${error.member.noMemberException}")
    private String NO_MEMBER_EXCEPTION;

    @ExceptionHandler(MaxMemberException.class)
    public ResponseEntity<ResponseDto> maxMemberEx(MaxMemberException e) {

        log.debug("Exception: noMemberEx");
        ResponseDto message = new ResponseDto(MAX_MEMBER_EXCEPTION, "Exceeding The Maximum number of members");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ExceptionHandler(NoMemberException.class)
    public ResponseEntity<ResponseDto> noMemberEx(NoMemberException e) {

        log.debug("Exception: noMemberEx");
        ResponseDto message = new ResponseDto(NO_MEMBER_EXCEPTION, e.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
