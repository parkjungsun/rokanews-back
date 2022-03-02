package com.pjsun.MilCoevo.domain.group.controller;

import com.pjsun.MilCoevo.domain.group.dto.*;
import com.pjsun.MilCoevo.domain.group.service.GroupService;
import com.pjsun.MilCoevo.domain.group.service.GroupServiceImpl;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.member.service.MemberServiceImpl;
import com.pjsun.MilCoevo.dto.ResponseDto;
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
@RequestMapping("/api/group")
public class GroupController {

    private final MemberService memberService;
    private final GroupService groupService;

    @Value("${error.common.bindingError}")
    private String BINDING_ERROR_EXCEPTION;

    @Value("${success.common.response}")
    private String SUCCESS_RESPONSE;

    @ApiOperation(value = "그룹 추가")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping
    public ResponseEntity<ResponseDto> create (
            @Validated @RequestBody GroupCreateRequestDto requestDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Long groupId = groupService.createGroup(
                requestDto.getGroupName(),
                requestDto.getPosition(),
                requestDto.getNickname());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, groupId);

        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @ApiOperation(value = "그룹 가입")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register (
            @Validated @RequestBody GroupRegisterRequestDto requestDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Long memberId = groupService.registerGroup(
                requestDto.getInviteCode(),
                requestDto.getPosition(),
                requestDto.getNickname());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, memberId);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 초대코드 확인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/register/{inviteCode}")
    public ResponseEntity<ResponseDto> confirmationGroup (
            @PathVariable String inviteCode) {

        String groupName = groupService.getGroupByInviteCode(inviteCode).getGroupName();

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, groupName);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 상세")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseDto> groupInfo (
            @PathVariable Long groupId) {

        GroupInfoResponseDto groupInfo = groupService.getGroupInfo(groupId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, groupInfo);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 정보 업데이트")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @PatchMapping("/{groupId}/name")
    public ResponseEntity<ResponseDto> updateGroupName (
            @PathVariable Long groupId,
            @Validated @RequestBody UpdateGroupNameRequestDto requestDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        String newGroupName = groupService.updateGroupName(groupId, requestDto.getGroupName());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, newGroupName);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 초대코드 업데이트")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @PatchMapping("/{groupId}/code")
    public ResponseEntity<ResponseDto> updateInviteCode (
            @PathVariable Long groupId) {

        String newInviteCode = groupService.updateInviteCode(groupId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, newInviteCode);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 멤버 전체 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{groupId}/member")
    public ResponseEntity<ResponseDto> getMembers(
            @PathVariable Long groupId,
            @ModelAttribute SearchGroupMemberDto searchCondition,
            Pageable pageable) {

        Page<MemberGroupDto> members = groupService.getMembers(groupId, searchCondition, pageable);
        log.debug("searchName= {}", searchCondition.getSearchName());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, members);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 멤버 권한 변경")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @PatchMapping("/{groupId}/member/{memberId}")
    public ResponseEntity<ResponseDto> updateMemberRank(
            @PathVariable Long memberId,
            @PathVariable Long groupId,
            @Validated @RequestBody UpdateMemberRankDto rankDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Long id = memberService.updateMemberRank(groupId, memberId, rankDto.getRank());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, id);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 멤버 차단")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/{groupId}/member/{memberId}")
    public ResponseEntity<ResponseDto> banMember(
            @PathVariable Long groupId,
            @PathVariable Long memberId) {

        memberService.banMember(groupId, memberId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
