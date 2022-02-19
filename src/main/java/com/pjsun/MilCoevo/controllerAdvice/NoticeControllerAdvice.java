package com.pjsun.MilCoevo.controllerAdvice;

import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.MaxMemberException;
import com.pjsun.MilCoevo.exception.NoCommentException;
import com.pjsun.MilCoevo.exception.NoNoticeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class NoticeControllerAdvice {

    @Value("${error.notice.noNoticeException}")
    private String NO_NOTICE_EXCEPTION;

    @Value("${error.notice.noCommentException}")
    private String NO_COMMENT_EXCEPTION;

    @ExceptionHandler(NoNoticeException.class)
    public ResponseEntity<ResponseDto> noNoticeEx(NoNoticeException e) {

        log.debug("Exception: noNoticeEx");
        ResponseDto message = new ResponseDto(NO_NOTICE_EXCEPTION, e.getMessage());

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ExceptionHandler(NoCommentException.class)
    public ResponseEntity<ResponseDto> noCommentEx(NoCommentException e) {

        log.debug("Exception: noCommentEx");
        ResponseDto message = new ResponseDto(NO_COMMENT_EXCEPTION, e.getMessage());

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
