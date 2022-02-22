package com.pjsun.MilCoevo.domain.member;


import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.domain.BaseEntity;
import com.pjsun.MilCoevo.domain.BooleanToYNConverter;
import com.pjsun.MilCoevo.domain.group.Group;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Slf4j
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
@ToString(exclude = {"user", "group"})
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Embedded
    private Identification info;

    @Enumerated(EnumType.STRING)
    private Rank rank;

    @Enumerated(EnumType.STRING)
    private PresentStatus presentStatus;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(length = 1)
    private boolean isAvailable;

    private LocalDateTime lastVisitedDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    /* 연관관계 메서드 */
    public void setUser(User user) {
        this.user = user;
        user.getMembers().add(this);
    }

    public void setGroup(Group group) {
        this.group = group;
        group.getMembers().add(this);
    }

    /* 생성자 */
    @Builder(builderClassName = "of", builderMethodName = "of")
    private Member(Identification info, Rank rank, PresentStatus presentStatus,
                   boolean isAvailable, User user, Group group) {
        this.info = info;
        this.rank = rank;
        this.presentStatus = presentStatus;
        this.isAvailable = isAvailable;

        if(!ObjectUtils.isEmpty(user)) {
            setUser(user);
        }
        if(!ObjectUtils.isEmpty(group)) {
            setGroup(group);
        }
    }

    @Builder(builderClassName = "createToMemberBuilder", builderMethodName = "createToMemberBuilder")
    public static Member createToMember(Identification info, User user, Group group) {
        return createMember(info, user, group, Rank.MEMBER);
    }

    @Builder(builderClassName = "createToLeaderBuilder", builderMethodName = "createToLeaderBuilder")
    public static Member createToLeader(Identification info, User user, Group group) {
        return createMember(info, user, group, Rank.LEADER);
    }

    private static Member createMember(Identification info, User user, Group group, Rank rank) {
        Assert.notNull(info, () -> "[Member] identification must not be null");

        return Member.of()
                .info(info).user(user)
                .group(group).rank(rank)
                .presentStatus(PresentStatus.MISSED)
                .isAvailable(true)
                .build();
    }

    /* 비즈니스 로직 */
    public Identification sign() {
        return Identification.createIdentificationBuilder()
                .email(info.getEmail())
                .position(info.getPosition())
                .nickname(info.getNickname())
                .build();
    }

    public void visitToGroup() {
        this.lastVisitedDate = LocalDateTime.now();
    }

    public void changeAvailability(boolean condition) {
        this.isAvailable = condition;
    }

    public void updateRank(Rank rank) {
        this.rank = rank;
    }

    public void changePresentStatus(PresentStatus presentStatus) {
        Assert.notNull(presentStatus, () -> "[Member] presentStatus must not be null");

        this.presentStatus = presentStatus;
    }
    public void updatePositionAndNickname(String position, String nickname) {
        this.info.updatePosition(position);
        this.info.updateNickname(nickname);
    }
}
