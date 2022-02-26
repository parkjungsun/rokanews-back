package com.pjsun.MilCoevo.domain.absence;

import com.pjsun.MilCoevo.domain.BaseEntity;
import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.member.Identification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Slf4j
@Entity @Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "absence")
@ToString(exclude = {"group"})
public class Absence extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "absence_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 4096)
    private String content;

    @Enumerated(EnumType.STRING)
    private Reason reason;

    @Enumerated(EnumType.STRING)
    private ProcessStatus processStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime decisionDate;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    /* 연관관계 메서드 */
    public void setGroup(Group group) {
        this.group = group;
        group.getAbsences().add(this);
    }

    /* 생성자 */
    @Builder(builderClassName = "of", builderMethodName = "of")
    private Absence(String title, String content, Reason reason, ProcessStatus processStatus,
                    LocalDate startDate, LocalDate endDate, Identification drafter, Group group) {
        this.title = title;
        this.content = content;
        this.reason = reason;
        this.processStatus = processStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.drafter = drafter;

        if(!ObjectUtils.isEmpty(group)) {
            setGroup(group);
        }
    }

    @Builder(builderClassName = "createAbsenceBuilder", builderMethodName = "createAbsenceBuilder")
    public static Absence createAbsence(
            String title, String content, Reason reason, LocalDate startDate,
            LocalDate endDate, Identification drafter, Group group) {
        Assert.hasText(title, () -> "[Absence] title must not be empty");
        Assert.notNull(content, () -> "[Absence] content must not be null");
        Assert.notNull(reason, () -> "[Absence] reason must not be null");
        Assert.notNull(startDate, () -> "[Absence] startDate must not be null");
        Assert.notNull(endDate, () -> "[Absence] endDate must not be null");
        Assert.notNull(drafter, () -> "[Absence] drafter must not be null");

        return Absence.of()
                .title(title).content(content).reason(reason)
                .processStatus(ProcessStatus.SUGGESTED)
                .startDate(startDate).endDate(endDate)
                .drafter(drafter).group(group)
                .build();
    }

    /* 비즈니스 로직 */
    public void decide(Identification sign, ProcessStatus processStatus) {
        Assert.notNull(sign, () -> "[Absence] sign must not be null");
        Assert.notNull(processStatus, () -> "[Absence] processStatus must not be null");

        this.arbiter = sign;
        this.processStatus = processStatus;
        this.decisionDate = LocalDateTime.now();
    }

}
