package com.pjsun.MilCoevo.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pjsun.MilCoevo.domain.schedule.Schedule;
import com.pjsun.MilCoevo.domain.schedule.WorkScope;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String drafterEmail;
    private String drafterPosition;
    private String drafterNickname;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String arbiterEmail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String arbiterPosition;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String arbiterNickname;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime decisionDate;

    @QueryProjection
    public ScheduleResponseDto(
            Long id, String title, WorkScope workScope,
            LocalDateTime workDate, String drafterPosition,
            String drafterNickname) {
        this.id = id;
        this.title = title;
        this.workScope = workScope;
        this.workDate = workDate;
        this.drafterPosition = drafterPosition;
        this.drafterNickname = drafterNickname;
    }

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.workScope = schedule.getWorkScope();
        this.workDate = schedule.getWorkDate();

        this.drafterEmail = schedule.getDrafter().getEmail();
        this.drafterPosition = schedule.getDrafter().getPosition();
        this.drafterNickname = schedule.getDrafter().getNickname();

        this.arbiterEmail = schedule.getArbiter().getEmail();
        this.arbiterPosition = schedule.getArbiter().getPosition();
        this.arbiterNickname = schedule.getArbiter().getNickname();

        this.createdDate = schedule.getCreatedDate();
        this.decisionDate = schedule.getDecisionDate();
    }
}
