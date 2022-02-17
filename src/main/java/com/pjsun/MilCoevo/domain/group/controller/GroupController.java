package com.pjsun.MilCoevo.domain.group.controller;

import com.pjsun.MilCoevo.domain.group.dto.*;
import com.pjsun.MilCoevo.domain.group.service.GroupService;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
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
@RequestMapping("/api/group")
public class GroupController {

    private final MemberService memberService;
    private final GroupService groupService;

    @Value("${error.common.bindingError}")
    private String BINDING_ERROR_EXCEPTION;

    @Value("${success.common.response}")
    private String SUCCESS_RESPONSE;

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

    @GetMapping("/register/{inviteCode}")
    public ResponseEntity<ResponseDto> confirmationGroup (
            @PathVariable String inviteCode) {

        String groupName = groupService.getGroupByInviteCode(inviteCode).getGroupName();

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, groupName);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseDto> groupInfo (
            @PathVariable Long groupId) {

        GroupInfoResponseDto groupInfo = groupService.getGroupInfo(groupId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, groupInfo);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

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

    @PatchMapping("/{groupId}/code")
    public ResponseEntity<ResponseDto> updateInviteCode (
            @PathVariable Long groupId) {

        String newInviteCode = groupService.updateInviteCode(groupId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, newInviteCode);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{groupId}/member")
    public ResponseEntity<ResponseDto> getMembers(
            @PathVariable Long groupId,
            Pageable pageable) {

        Page<MemberGroupDto> members = groupService.getMembers(groupId, pageable);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, members);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PatchMapping("/{groupId}/member/{memberId}")
    public ResponseEntity<ResponseDto> updateMemberRank(
            @PathVariable Long memberId,
            @Validated @RequestBody UpdateMemberRankDto rankDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Long id = memberService.updateMemberRank(memberId, rankDto.getRank());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, id);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/member/{memberId}")
    public ResponseEntity<ResponseDto> banMember(
            @PathVariable Long memberId) {

        memberService.banMember(memberId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
