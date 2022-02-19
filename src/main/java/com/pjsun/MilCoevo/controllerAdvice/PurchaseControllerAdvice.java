package com.pjsun.MilCoevo.controllerAdvice;

import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.MaxMemberException;
import com.pjsun.MilCoevo.exception.NoPurchaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class PurchaseControllerAdvice {

    @Value("${error.purchase.noPurchaseException}")
    private String NO_PURCHASE_EXCEPTION;

    @ExceptionHandler(NoPurchaseException.class)
    public ResponseEntity<ResponseDto> noPurchaseEx(NoPurchaseException e) {

        log.debug("Exception: noPurchaseEx");
        ResponseDto message = new ResponseDto(NO_PURCHASE_EXCEPTION, e.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
