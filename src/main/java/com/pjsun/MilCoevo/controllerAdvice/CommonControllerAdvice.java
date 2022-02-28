package com.pjsun.MilCoevo.controllerAdvice;

import com.fasterxml.jackson.core.JsonParseException;
import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.*;
import com.querydsl.core.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonControllerAdvice {

    @Value("${error.common.jsonParseException}")
    private String JSON_PARSE_EXCEPTION;

    @Value("${error.common.noTokenException}")
    private String NO_TOKEN_EXCEPTION;

    @Value("${error.common.invalidTokenException}")
    private String INVALID_TOKEN_EXCEPTION;

    @Value("${error.common.httpMessageNotReadableException}")
    private String HTTP_MESSAGE_NOT_READABLE_EXCEPTION;

    @Value("${error.common.nonUniqueResultException}")
    private String NON_UNIQUE_RESULT_EXCEPTION;

    @Value("${error.common.forbiddenException}")
    private String FORBIDDEN_EXCEPTION;

    @Value("${error.common.illegalPathException}")
    private String ILLEGAL_PATH_EXCEPTION;

    @Value("${error.common.noPermissionException}")
    private String NO_PERMISSION_EXCEPTION;

    @Value("${error.common.oAuthProviderMissMatchException}")
    private String OAUTH_PROVIDER_MISS_MATCH_EXCEPTION;

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<ResponseDto> nonUniqueEx(NonUniqueResultException e) {

        log.debug("Exception: nonUniqueEx");
        ResponseDto error = new ResponseDto(NON_UNIQUE_RESULT_EXCEPTION,"INTERNAL SERVER ERROR");

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ResponseDto> jsonParseEx(JsonParseException e) {

        log.debug("Exception: jsonParseEx");
        ResponseDto error = new ResponseDto(JSON_PARSE_EXCEPTION,"Invalid Parameter Request");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoTokenException.class)
    public ResponseEntity<ResponseDto> noTokenEx(NoTokenException e) {

        log.debug("Exception: noTokenEx");
        ResponseDto error = new ResponseDto(NO_TOKEN_EXCEPTION, "No Authentication Token");

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseDto> forbiddenEx(ForbiddenException e) {

        log.debug("Exception: forbiddenEx");
        ResponseDto error = new ResponseDto(FORBIDDEN_EXCEPTION, "No Authorization");

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ResponseDto> invalidTokenEx(InvalidTokenException e) {

        log.debug("Exception: invalidTokenEx");
        ResponseDto error = new ResponseDto(INVALID_TOKEN_EXCEPTION, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalPathException.class)
    public ResponseEntity<ResponseDto> illegalPathEx(IllegalPathException e) {

        log.debug("Exception: illegalPathEx");
        ResponseDto error = new ResponseDto(ILLEGAL_PATH_EXCEPTION, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto> httpMessageNotReadableEx(HttpMessageNotReadableException e) {

        log.debug("Exception: httpMessageNotReadableEx");
        ResponseDto error = new ResponseDto(HTTP_MESSAGE_NOT_READABLE_EXCEPTION, "Invalid Parameter Request");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<ResponseDto> noPermissionEx(NoPermissionException e) {

        log.debug("Exception: noPermissionEx");
        ResponseDto error = new ResponseDto(NO_PERMISSION_EXCEPTION, "No Permission");

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(OAuthProviderMissMatchException.class)
    public ResponseEntity<ResponseDto> oAuthProviderMissMatchEx(OAuthProviderMissMatchException e) {

        log.debug("Exception: oAuthProviderMissMatchEx");
        ResponseDto error = new ResponseDto(OAUTH_PROVIDER_MISS_MATCH_EXCEPTION, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
