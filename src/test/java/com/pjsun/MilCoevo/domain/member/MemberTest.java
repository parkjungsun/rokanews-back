package com.pjsun.MilCoevo.domain.member;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.test.DomainTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberTest extends DomainTest {

    @Test
    @DisplayName("팀원 권한 멤버 생성")
    void createToMemberTest() {
        //given
        String email = "test@test.com";
        String password = "password";
        String position = "position";
        String nickname = "nickname";
        String groupName = "groupName";

        User user = User.createByUser(email, password);
        Group group = Group.createGroupBuilder(groupName);
        Identification info = Identification.createIdentificationBuilder()
                .email(email).position(position).nickname(nickname).build();

        //when
        Member member = Member.createToMemberBuilder()
                .info(info).user(user).group(group).build();

        //then
        assertThat(member.getInfo()).isEqualTo(info);
        assertThat(member.getGroup()).isEqualTo(group);
        assertThat(member.getUser()).isEqualTo(user);
        assertThat(member.getRank()).isEqualTo(Rank.MEMBER);
        assertThat(member.getPresentStatus()).isEqualTo(PresentStatus.MISSED);
        assertThat(member.isAvailable()).isEqualTo(true);
    }

    @Test
    @DisplayName("팀장 권한 멤버 생성")
    void createToLeaderTest() {
        //given
        String email = "test@test.com";
        String password = "password";
        String position = "position";
        String nickname = "nickname";
        String groupName = "groupName";

        User user = User.createByUser(email, password);
        Group group = Group.createGroupBuilder(groupName);
        Identification info = Identification.createIdentificationBuilder()
                .email(email).position(position).nickname(nickname).build();

        //when
        Member member = Member.createToLeaderBuilder()
                .info(info).user(user).group(group).build();

        //then
        assertThat(member.getInfo()).isEqualTo(info);
        assertThat(member.getGroup()).isEqualTo(group);
        assertThat(member.getUser()).isEqualTo(user);
        assertThat(member.getRank()).isEqualTo(Rank.LEADER);
        assertThat(member.getPresentStatus()).isEqualTo(PresentStatus.MISSED);
        assertThat(member.isAvailable()).isEqualTo(true);
    }

    @Test
    @DisplayName("비즈니스 로직")
    void businessTest() {
        //given
        String email = "test@test.com";
        String password = "password";
        String position = "position";
        String nickname = "nickname";
        String groupName = "groupName";
        String updatePosition = "updatePosition";
        String updateNickname = "updateNickname";

        User user = User.createByUser(email, password);
        Group group = Group.createGroupBuilder(groupName);
        Identification info = Identification.createIdentificationBuilder()
                .email(email).position(position).nickname(nickname).build();

        Member member = Member.createToLeaderBuilder()
                .info(info).user(user).group(group).build();

        //when
        member.visitToGroup();
        member.changeAvailability(false);
        member.updateRank(Rank.OTHERS);
        member.changePresentStatus(PresentStatus.STEPOUT);
        member.updatePositionAndNickname(updatePosition, updateNickname);

        //then
        assertThat(member.getRank()).isEqualTo(Rank.OTHERS);
        assertThat(member.getPresentStatus()).isEqualTo(PresentStatus.STEPOUT);
        assertThat(member.isAvailable()).isEqualTo(false);
        assertThat(member.getInfo().getPosition()).isEqualTo(updatePosition);
        assertThat(member.getInfo().getNickname()).isEqualTo(updateNickname);
        assertThat(member.getLastVisitedDate()).isNotNull();
    }
}