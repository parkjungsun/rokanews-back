package com.pjsun.MilCoevo.domain.purchase.controller;

import com.pjsun.MilCoevo.domain.purchase.dto.*;
import com.pjsun.MilCoevo.domain.purchase.service.PurchaseService;
import com.pjsun.MilCoevo.domain.purchase.service.PurchaseServiceImpl;
import com.pjsun.MilCoevo.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Value("${error.common.bindingError}")
    private String BINDING_ERROR_EXCEPTION;

    @Value("${success.common.response}")
    private String SUCCESS_RESPONSE;

    @PostMapping("/{groupId}")
    public ResponseEntity<ResponseDto> addPurchase(
            @PathVariable Long groupId,
            @Validated @RequestBody PurchaseAddRequestDto requestDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Long purchaseId = purchaseService.addPurchase(
                groupId, requestDto.getTitle(), requestDto.getContent(),
                requestDto.getPurpose(), requestDto.getPurchaseDate(),
                requestDto.getItems());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, purchaseId);

        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}/detail/{purchaseId}")
    public ResponseEntity<ResponseDto> getPurchase(@PathVariable Long purchaseId) {

        PurchaseResponseDto purchase = purchaseService.getPurchase(purchaseId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, purchase);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseDto> getPurchases(
            @PathVariable Long groupId,
            @ModelAttribute SearchPurchaseDto searchCondition,
            Pageable pageable) {

        PurchasesResponseDto purchases = purchaseService
                .getPurchases(groupId, searchCondition, pageable);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, purchases);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PatchMapping("/{groupId}/detail/{purchaseId}")
    public ResponseEntity<ResponseDto> updatePurchase(
            @PathVariable Long groupId,
            @PathVariable Long purchaseId,
            @Validated @RequestBody PurchaseUpdateRequestDto requestDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        purchaseService.updatePurchase(groupId, purchaseId, requestDto.getProcessStatus());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
