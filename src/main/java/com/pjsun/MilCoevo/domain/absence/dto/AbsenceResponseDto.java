package com.pjsun.MilCoevo.domain.absence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pjsun.MilCoevo.domain.absence.Absence;
import com.pjsun.MilCoevo.domain.absence.Reason;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AbsenceResponseDto {

    private Long id;
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private Reason reason;

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
    public AbsenceResponseDto(
            Long id, String title, LocalDate startDate, LocalDate endDate,
            Reason reason, String drafterPosition, String drafterNickname) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.drafterPosition = drafterPosition;
        this.drafterNickname = drafterNickname;
    }

    public AbsenceResponseDto(Absence absence) {
        this.id = absence.getId();
        this.title = absence.getTitle();
        this.content = absence.getContent();
        this.startDate = absence.getStartDate();
        this.endDate = absence.getEndDate();
        this.reason = absence.getReason();

        this.drafterEmail = absence.getDrafter().getEmail();
        this.drafterPosition = absence.getDrafter().getPosition();
        this.drafterNickname = absence.getDrafter().getNickname();

        this.arbiterEmail = absence.getArbiter().getEmail();
        this.arbiterPosition = absence.getArbiter().getPosition();
        this.arbiterNickname = absence.getArbiter().getNickname();

        this.createdDate = absence.getCreatedDate();
        this.decisionDate = absence.getDecisionDate();
    }
}
