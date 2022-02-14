package com.pjsun.MilCoevo.domain.member.controller;

import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.user.service.UserService;
import com.pjsun.MilCoevo.dto.ResponseDto;
import com.pjsun.MilCoevo.exception.InvalidTokenException;
import com.pjsun.MilCoevo.exception.NoTokenException;
import com.pjsun.MilCoevo.util.SecurityUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header") })
    @GetMapping
    public ResponseEntity<ResponseDto> Members() throws InvalidTokenException {

        List<MemberGroupDto> members = memberService.getMembersByUser();

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, members);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
