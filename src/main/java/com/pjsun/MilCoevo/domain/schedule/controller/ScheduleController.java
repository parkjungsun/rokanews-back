package com.pjsun.MilCoevo.domain.schedule.controller;

import com.pjsun.MilCoevo.domain.schedule.dto.ScheduleAddRequestDto;
import com.pjsun.MilCoevo.domain.schedule.dto.ScheduleResponseDto;
import com.pjsun.MilCoevo.domain.schedule.dto.ScheduleUpdateRequestDto;
import com.pjsun.MilCoevo.domain.schedule.dto.SearchScheduleDto;
import com.pjsun.MilCoevo.domain.schedule.service.ScheduleService;
import com.pjsun.MilCoevo.domain.schedule.service.ScheduleServiceImpl;
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

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Value("${error.common.bindingError}")
    private String BINDING_ERROR_EXCEPTION;

    @Value("${success.common.response}")
    private String SUCCESS_RESPONSE;

    @ApiOperation(value = "그룹 스케줄 추가")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/{groupId}")
    public ResponseEntity<ResponseDto> addSchedule(
            @PathVariable Long groupId,
            @Validated @RequestBody ScheduleAddRequestDto requestDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Long scheduleId = scheduleService.addSchedule(groupId, requestDto.getTitle(),
                requestDto.getContent(), requestDto.getWorkScope(), requestDto.getWorkDate());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, scheduleId);

        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @ApiOperation(value = "그룹 스케줄 상세 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{groupId}/detail/{scheduleId}")
    public ResponseEntity<ResponseDto> getSchedule(@PathVariable Long scheduleId) {

        ScheduleResponseDto schedule = scheduleService.getSchedule(scheduleId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, schedule);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 스케줄 전체 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/{groupId}/list")
    public ResponseEntity<ResponseDto> getSchedules(
            @PathVariable Long groupId,
            @RequestBody SearchScheduleDto searchCondition,
            Pageable pageable) {

        Page<ScheduleResponseDto> schedules = scheduleService
                .getSchedules(groupId, searchCondition, pageable);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, schedules);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 스케줄 상세 업데이트")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "String", paramType = "header")
    })
    @PatchMapping("/{groupId}/detail/{scheduleId}")
    public ResponseEntity<ResponseDto> updateSchedule(
            @PathVariable Long groupId,
            @PathVariable Long scheduleId,
            @Validated @RequestBody ScheduleUpdateRequestDto responseDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        scheduleService.updateSchedule(groupId, scheduleId, responseDto.getProcessStatus());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
