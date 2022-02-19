package com.pjsun.MilCoevo.domain.schedule.dto;

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
    private String content;
    private WorkScope workScope;
    private LocalDateTime workDate;

    private String drafterEmail;
    private String drafterPosition;
    private String drafterNickname;

    private String arbiterEmail;
    private String arbiterPosition;
    private String arbiterNickname;

    private LocalDateTime createdDate;
    private LocalDateTime decisionDate;

    @QueryProjection
    public ScheduleResponseDto(
            Long id, String title, String content, WorkScope workScope,
            LocalDateTime workDate, String drafterEmail, String drafterPosition,
            String drafterNickname, String arbiterEmail, String arbiterPosition,
            String arbiterNickname, LocalDateTime createdDate, LocalDateTime decisionDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.workScope = workScope;
        this.workDate = workDate;
        this.drafterEmail = drafterEmail;
        this.drafterPosition = drafterPosition;
        this.drafterNickname = drafterNickname;
        this.arbiterEmail = arbiterEmail;
        this.arbiterPosition = arbiterPosition;
        this.arbiterNickname = arbiterNickname;
        this.createdDate = createdDate;
        this.decisionDate = decisionDate;
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
