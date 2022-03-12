package com.pjsun.MilCoevo.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.schedule.Schedule;
import com.pjsun.MilCoevo.domain.schedule.WorkScope;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleResponseDto {

    private Long id;
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;
    private WorkScope workScope;
    private LocalDateTime workDate;

    private ProcessStatus processStatus;

    private String drafterEmail;
    private String drafterPosition;
    private String drafterNickname;

    private String arbiterEmail;
    private String arbiterPosition;
    private String arbiterNickname;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime decisionDate;

    @QueryProjection
    public ScheduleResponseDto(
            Long id, String title, WorkScope workScope,
            LocalDateTime workDate, String drafterPosition,
            String drafterNickname, ProcessStatus processStatus) {
        this.id = id;
        this.title = title;
        this.workScope = workScope;
        this.workDate = workDate;
        this.drafterPosition = drafterPosition;
        this.drafterNickname = drafterNickname;
        this.processStatus = processStatus;
    }

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.workScope = schedule.getWorkScope();
        this.workDate = schedule.getWorkDate();
        this.processStatus = schedule.getProcessStatus();
        this.drafterEmail = schedule.getDrafter().getEmail();
        this.drafterPosition = schedule.getDrafter().getPosition();
        this.drafterNickname = schedule.getDrafter().getNickname();

        if(!ObjectUtils.isEmpty(schedule.getArbiter())) {
            this.arbiterEmail = schedule.getArbiter().getEmail();
            this.arbiterPosition = schedule.getArbiter().getPosition();
            this.arbiterNickname = schedule.getArbiter().getNickname();
        }

        this.createdDate = schedule.getCreatedDate();
        this.decisionDate = schedule.getDecisionDate();
    }
}
