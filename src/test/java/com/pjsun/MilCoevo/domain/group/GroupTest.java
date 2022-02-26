package com.pjsun.MilCoevo.domain.group;

import com.pjsun.MilCoevo.test.DomainTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupTest extends DomainTest {

    @Test
    @DisplayName("그룹 생성")
    void createGroupTest() {
        //given
        String groupName = "groupName";

        //when
        Group group = Group.createGroupBuilder(groupName);

        //then
        assertThat(group.getGroupName()).isEqualTo(groupName);
        assertThat(group.getInviteCode()).isNotNull();
        assertThat(group.isAvailable()).isEqualTo(true);
    }

    @Test
    @DisplayName("비즈니스 로직")
    void businessTest() {
        //given
        String groupName = "groupName";
        Group group = Group.createGroupBuilder(groupName);

        String updateGroupName = "updateGroupName";
        String inviteCode = group.getInviteCode();

        //when
        group.updateInviteCode();
        group.updateGroupName(updateGroupName);
        group.changeAvailability(false);

        //then
        assertThat(group.getGroupName()).isEqualTo(updateGroupName);
        assertThat(group.getInviteCode()).isNotEqualTo(inviteCode);
        assertThat(group.isAvailable()).isEqualTo(false);
    }
}