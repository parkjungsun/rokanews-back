package com.pjsun.MilCoevo.domain.schedule;

import com.pjsun.MilCoevo.domain.BaseEntity;
import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.member.Identification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Slf4j
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedules")
@ToString(exclude = {"group"})
public class Schedule extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 4096)
    private String content;

    private LocalDateTime workDate;

    @Enumerated(EnumType.STRING)
    private WorkScope workScope;

    @Enumerated(EnumType.STRING)
    private ProcessStatus processStatus;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "drafter_email", nullable = false)),
            @AttributeOverride(name = "position", column = @Column(name = "drafter_position", nullable = false)),
            @AttributeOverride(name = "nickname", column = @Column(name = "drafter_nickname", nullable = false))
    })
    private Identification drafter;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "arbiter_email")),
            @AttributeOverride(name = "position", column = @Column(name = "arbiter_position")),
            @AttributeOverride(name = "nickname", column = @Column(name = "arbiter_nickname"))
    })
    private Identification arbiter;

    private LocalDateTime decisionDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    /* 연관관계 메서드 */
    public void setGroup(Group group) {
        this.group = group;
        group.getSchedules().add(this);
    }

    /* 생성자 */
    @Builder(builderClassName = "of", builderMethodName = "of")
    private Schedule(String title, String content, WorkScope workScope, LocalDateTime workDate,
                     ProcessStatus processStatus, Identification drafter, Group group) {
        this.title = title;
        this.content = content;
        this.workScope = workScope;
        this.workDate = workDate;
        this.processStatus = processStatus;
        this.drafter = drafter;

        if(!ObjectUtils.isEmpty(group)) {
            setGroup(group);
        }
    }

    @Builder(builderClassName = "createScheduleBuilder", builderMethodName = "createScheduleBuilder")
    public static Schedule createSchedule(String title, String content, WorkScope workScope,
                                          LocalDateTime workDate, Identification drafter, Group group) {
        Assert.hasText(title, () -> "[Schedule] title must not be empty");
        Assert.notNull(content, () -> "[Schedule] content must not be null");
        Assert.notNull(workScope, () -> "[Schedule] workScope must not be null");
        Assert.notNull(workDate, () -> "[Schedule] workDate must not be null");
        Assert.notNull(drafter, () -> "[Schedule] drafter must not be null");

        return Schedule.of()
                .title(title).content(content)
                .workScope(workScope).workDate(workDate)
                .processStatus(ProcessStatus.SUGGESTED)
                .drafter(drafter).group(group)
                .build();
    }

    /* 비즈니스 로직 */
    public void decide(Identification sign, ProcessStatus processStatus) {
        Assert.notNull(sign, () -> "[Schedule] sign must not be null");
        Assert.notNull(processStatus, () -> "[Schedule] processStatus must not be null");

        this.arbiter = sign;
        this.processStatus = processStatus;
        this.decisionDate = LocalDateTime.now();
    }

}
