package com.pjsun.MilCoevo.domain.member.controller;

import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.dto.MemberResponseDto;
import com.pjsun.MilCoevo.domain.member.dto.UpdateMemberRequestDto;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.member.service.MemberServiceImpl;
import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.InvalidTokenException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Value("${error.common.bindingError}")
    private String BINDING_ERROR_EXCEPTION;

    @Value("${success.common.response}")
    private String SUCCESS_RESPONSE;

    @ApiOperation(value = "사용자 가입 그룹 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken",
                    required = true, dataType = "String", paramType = "header") })
    @GetMapping
    public ResponseEntity<ResponseDto> getMembers(
            Pageable pageable) throws InvalidTokenException {

        Page<MemberGroupDto> members = memberService.getMembersByUser(pageable);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, members);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseDto> getMember(@PathVariable Long groupId) {

        Member result = memberService.getMemberByUserAndGroup(groupId);
        MemberResponseDto member = new MemberResponseDto(result);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, member);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<ResponseDto> getMember(
            @PathVariable Long groupId,
            @Validated @RequestBody UpdateMemberRequestDto requestDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Long memberId = memberService
                .updateMember(groupId, requestDto.getPosition(), requestDto.getNickname());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, memberId);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<ResponseDto> removeMember(@PathVariable Long groupId) {

        memberService.removeMember(groupId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
