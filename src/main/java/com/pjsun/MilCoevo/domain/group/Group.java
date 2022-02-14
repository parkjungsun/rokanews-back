package com.pjsun.MilCoevo.domain.group;

import com.pjsun.MilCoevo.domain.BaseEntity;
import com.pjsun.MilCoevo.domain.BooleanToYNConverter;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.purchase.Purchase;
import com.pjsun.MilCoevo.domain.schedule.Schedule;
import com.pjsun.MilCoevo.domain.absence.Absence;
import com.pjsun.MilCoevo.domain.notice.Notice;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "groups")
@ToString(exclude = {
        "keywords", "members", "schedules",
        "absences", "purchases", "notices"
})
public class Group extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "group_id")
    private Long id;

    @Column(unique = true, length = 50)
    private String inviteCode;

    @Column(length = 50)
    private String groupName;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(length = 1)
    private boolean isAvailable;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keyword> keywords = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Absence> absences = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Purchase> purchases = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Notice> notices = new ArrayList<>();

    /* 연관관계 메서드 */
    public void addMember(Member member) {
        this.members.add(member);
        member.setGroup(this);
    }

    public void addKeyword(Keyword keyword) {
        this.keywords.add(keyword);
        keyword.setGroup(this);
    }

    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
        schedule.setGroup(this);
    }

    public void addAbsence(Absence absence) {
        this.absences.add(absence);
        absence.setGroup(this);
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
        purchase.setGroup(this);
    }

    public void addNotice(Notice notice) {
        this.notices.add(notice);
        notice.setGroup(this);
    }

    /* 생성자 */
    @Builder(builderClassName = "of", builderMethodName = "of")
    private Group(String inviteCode, String groupName, boolean isAvailable) {
        this.inviteCode = inviteCode;
        this.groupName = groupName;
        this.isAvailable = isAvailable;
    }

    public static Group createGroupBuilder(String groupName) {
        String inviteCode = createInviteCode();
        return createGroup(groupName, inviteCode);
    }

    private static Group createGroup(String groupName, String inviteCode) {
        Assert.hasText(groupName, () -> "[Group] groupName must not be empty");
        Assert.hasText(groupName, () -> "[Group] inviteCode must not be empty");

        return Group.of()
                .inviteCode(inviteCode).groupName(groupName)
                .isAvailable(true)
                .build();
    }

    /* 비즈니스 로직 */
    private static String createInviteCode() {
        String random = UUID.randomUUID().toString().substring(0, 8);

        String now = LocalDateTime.now().toString();
        String second = now.substring(17, 19);
        String nano = now.substring(20, 23);

        return second + random + nano;
    }

    public void updateInviteCode() {
        String newCode = createInviteCode();
        this.inviteCode = newCode;
    }

    public void updateGroupName(String groupName) {
        Assert.hasText(groupName, () -> "[Group] groupName must not be empty");

        this.groupName = groupName;
    }

    public void changeAvailability(boolean condition) {
        this.isAvailable = condition;
    }
}
