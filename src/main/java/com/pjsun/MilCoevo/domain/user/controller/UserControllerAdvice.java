package com.pjsun.MilCoevo.domain.user.controller;

import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.DuplicateUserException;
import com.pjsun.MilCoevo.exception.InactiveUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserControllerAdvice {

    @Value("${error.user.duplicateUserException}")
    private String DUPLICATE_USER_EXCEPTION;

    @Value("${error.user.userNotFoundException}")
    private String USER_NOT_FOUND_EXCEPTION;

    @Value("${error.user.inactiveUserException}")
    private String INACTIVE_USER_EXCEPTION;

    @Value("${error.user.badCredentialsException}")
    private String BAD_CREDENTIALS_EXCEPTION;

    @ExceptionHandler(InactiveUserException.class)
    public ResponseEntity<ResponseDto> inactiveUserEx(InactiveUserException e) {

        log.debug("Exception: inactiveUserEx");
        ResponseDto message = new ResponseDto(INACTIVE_USER_EXCEPTION, e.getMessage());

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ResponseDto> duplicateUserEx(DuplicateUserException e) {

        log.debug("Exception: duplicateUserEx");
        ResponseDto message = new ResponseDto(DUPLICATE_USER_EXCEPTION, e.getMessage());

        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseDto> userNotFoundEx(UsernameNotFoundException e) {

        log.debug("Exception: userNotFoundEx");
        ResponseDto message = new ResponseDto(USER_NOT_FOUND_EXCEPTION, e.getMessage());

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDto> badCredentialsEx(BadCredentialsException e) {

        log.debug("Exception: badCredentialsEx");
        ResponseDto message = new ResponseDto(BAD_CREDENTIALS_EXCEPTION, "Fail To Login");

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }
}
