package com.pjsun.MilCoevo.domain.notice.controller;

import com.pjsun.MilCoevo.domain.notice.dto.*;
import com.pjsun.MilCoevo.domain.notice.service.NoticeService;
import com.pjsun.MilCoevo.domain.notice.service.NoticeServiceImpl;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Value("${error.common.bindingError}")
    private String BINDING_ERROR_EXCEPTION;

    @Value("${success.common.response}")
    private String SUCCESS_RESPONSE;

    @PostMapping("/{groupId}")
    public ResponseEntity<ResponseDto> addNotice(
            @PathVariable Long groupId,
            @Validated @RequestBody NoticeAddRequestDto requestDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Long noticeId = noticeService.addNotice(groupId, requestDto.getTitle(), requestDto.getContent());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, noticeId);

        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}/detail/{noticeId}")
    public ResponseEntity<ResponseDto> getNotice(@PathVariable Long noticeId) {

        NoticeResponseDto notice = noticeService.getNotice(noticeId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, notice);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseDto> getNotices(
            @PathVariable Long groupId,
            @ModelAttribute SearchNoticeDto searchCondition,
            Pageable pageable) {

        Page<NoticeResponseDto> notices = noticeService
                .getNotices(groupId, searchCondition, pageable);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, notices);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PatchMapping("/{groupId}/detail/{noticeId}")
    public ResponseEntity<ResponseDto> updateNotice(
            @PathVariable Long groupId,
            @PathVariable Long noticeId,
            @Validated @RequestBody NoticeUpdateRequestDto responseDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        noticeService.updateNotice(groupId, noticeId, responseDto.getTitle(), responseDto.getContent());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/detail/{noticeId}")
    public ResponseEntity<ResponseDto> removeNotice(
            @PathVariable Long groupId,
            @PathVariable Long noticeId) {

        noticeService.removeNotice(groupId, noticeId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/detail/{noticeId}/comment")
    public ResponseEntity<ResponseDto> addComment(
            @PathVariable Long groupId,
            @PathVariable Long noticeId,
            @Validated @RequestBody CommentAddRequestDto requestDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Long commentId = noticeService.addComment(groupId, noticeId, requestDto.getComment());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, commentId);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/detail/{noticeId}/comment/{commentId}")
    public ResponseEntity<ResponseDto> addComment(
            @PathVariable Long groupId,
            @PathVariable Long commentId) {

        noticeService.removeComment(groupId, commentId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
