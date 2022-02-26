package com.pjsun.MilCoevo.domain.group.service;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.dto.GroupInfoResponseDto;
import com.pjsun.MilCoevo.domain.group.dto.SearchGroupMemberDto;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.repository.MemberRepository;
import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.domain.user.service.UserService;
import com.pjsun.MilCoevo.exception.InactiveGroupException;
import com.pjsun.MilCoevo.exception.MaxMemberException;
import com.pjsun.MilCoevo.exception.NoExistGroupException;
import com.pjsun.MilCoevo.test.MockTest;
import com.pjsun.MilCoevo.test.builder.GroupBuilder;
import com.pjsun.MilCoevo.test.builder.MemberBuilder;
import com.pjsun.MilCoevo.test.builder.UserBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class GroupServiceImplTest extends MockTest {

    @InjectMocks
    GroupServiceImpl groupService;

    @Mock
    UserService userService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    GroupRepository groupRepository;

    @Test
    @DisplayName("초대코드로 그룹 정보 가져오기")
    void getGroupByInviteCodeTest() {
        //given
        Group group = GroupBuilder.build(1L);

        given(groupRepository.findGroupByInviteCode(any())).willReturn(Optional.of(group));

        //when
        Group result = groupService.getGroupByInviteCode(group.getInviteCode());

        //then
        assertThat(result.getGroupName()).isEqualTo(group.getGroupName());
        assertThat(result.getInviteCode()).isEqualTo(group.getInviteCode());
    }

    @Test
    @DisplayName("초대코드로 그룹 정보 가져오기 실패 존재하지 않는 그룹")
    void getGroupByInviteCodeFailTest() {
        //given
        Group group = GroupBuilder.build(1L);

        given(groupRepository.findGroupByInviteCode(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> groupService.getGroupByInviteCode(group.getInviteCode()))
                .isInstanceOf(NoExistGroupException.class);
    }

    @Test
    @DisplayName("초대코드로 그룹 정보 가져오기 실패 존재하지 않는 그룹")
    void getGroupByInviteCodeFail2Test() {
        //given
        Group group = GroupBuilder.build(1L);
        group.changeAvailability(false);

        given(groupRepository.findGroupByInviteCode(any())).willReturn(Optional.of(group));

        //then
        assertThatThrownBy(() -> groupService.getGroupByInviteCode(group.getInviteCode()))
                .isInstanceOf(InactiveGroupException.class);
    }

    @Test
    @DisplayName("그룹에 가입하기 성공")
    void registerGroupTest() {
        //given
        User user = UserBuilder.build(1L);
        Member member = MemberBuilder.build(1L);
        Group group = GroupBuilder.build(1L);

        given(groupRepository.findGroupByInviteCode(any())).willReturn(Optional.of(group));
        given(userService.getUserFromContext()).willReturn(user);
        given(memberRepository.searchMemberByUserIdAndGroupId(any(),any())).willReturn(Optional.empty());
        given(memberRepository.save(any())).willReturn(member);

        //when
        Long id = groupService.registerGroup(
                group.getInviteCode(),
                member.getInfo().getPosition(),
                member.getInfo().getNickname());
    }

    @Test
    @DisplayName("그룹에 가입하기 실패 최대 가입 초과")
    void registerGroupFailTest() {
        //given
        User user = UserBuilder.build(1L);
        user.addMember(MemberBuilder.build(2L));
        user.addMember(MemberBuilder.build(3L));
        user.addMember(MemberBuilder.build(4L));
        user.addMember(MemberBuilder.build(5L));
        user.addMember(MemberBuilder.build(6L));
        Member member = MemberBuilder.build(1L);
        Group group = GroupBuilder.build(1L);

        given(userService.getUserFromContext()).willReturn(user);

        //then
        assertThatThrownBy(() -> groupService.registerGroup(
                group.getInviteCode(),
                member.getInfo().getPosition(),
                member.getInfo().getNickname())).isInstanceOf(MaxMemberException.class);
    }

    @Test
    @DisplayName("그룹 생성 성공")
    void createGroupTest() {
        //given
        User user = UserBuilder.build(1L);
        Group group = GroupBuilder.build(1L);
        Member member = MemberBuilder.build(1L);

        given(userService.getUserFromContext()).willReturn(user);
        given(groupRepository.save(any())).willReturn(group);
        given(memberRepository.save(any())).willReturn(member);

        //when
        Long id = groupService.createGroup(
                group.getGroupName(),
                member.getInfo().getPosition(),
                member.getInfo().getNickname());
    }

    @Test
    @DisplayName("그룹 생성 실패 최대 그룹 초과")
    void createGroupFailTest() {
        //given
        User user = UserBuilder.build(1L);
        user.addMember(MemberBuilder.build(2L));
        user.addMember(MemberBuilder.build(3L));
        user.addMember(MemberBuilder.build(4L));
        user.addMember(MemberBuilder.build(5L));
        user.addMember(MemberBuilder.build(6L));
        Group group = GroupBuilder.build(1L);
        Member member = MemberBuilder.build(1L);

        given(userService.getUserFromContext()).willReturn(user);

        //when
        assertThatThrownBy(() -> groupService.createGroup(
                group.getGroupName(),
                member.getInfo().getPosition(),
                member.getInfo().getNickname())).isInstanceOf(MaxMemberException.class);
    }

    @Test
    @DisplayName("그룹 정보 가져오기")
    void getGroupInfoTest() {
        //given
        Group group = GroupBuilder.build(1L);

        given(groupRepository.findById(any())).willReturn(Optional.of(group));

        //when
        GroupInfoResponseDto result = groupService.getGroupInfo(group.getId());

        //then
        assertThat(result.getInviteCode()).isEqualTo(group.getInviteCode());
        assertThat(result.getGroupName()).isEqualTo(group.getGroupName());
    }

    @Test
    @DisplayName("그룹 정보 가져오기 실패 없는 그룹")
    void getGroupInfoFailTest() {
        //given
        Group group = GroupBuilder.build(1L);

        given(groupRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> groupService.getGroupInfo(group.getId()))
                .isInstanceOf(NoExistGroupException.class);
    }

    @Test
    @DisplayName("그룹 정보 업데이트")
    void updateGroupNameTest() {
        //given
        Group group = GroupBuilder.build(1L);
        String updateGroupName = "updateGroupName";

        given(groupRepository.findById(any())).willReturn(Optional.of(group));

        //when
        String result = groupService.updateGroupName(group.getId(), updateGroupName);

        //then
        assertThat(result).isEqualTo(updateGroupName);
    }

    @Test
    @DisplayName("그룹 정보 업데이트 실패")
    void updateGroupNameFailTest() {
        //given
        Group group = GroupBuilder.build(1L);
        String updateGroupName = "updateGroupName";

        given(groupRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> groupService.updateGroupName(group.getId(), updateGroupName))
                .isInstanceOf(NoExistGroupException.class);
    }

    @Test
    @DisplayName("그룹 초대코드 초기화")
    void updateInviteCodeTest() {
        //given
        Group group = GroupBuilder.build(1L);
        String preInviteCode = group.getInviteCode();

        given(groupRepository.findById(any())).willReturn(Optional.of(group));

        //when
        String result = groupService.updateInviteCode(group.getId());

        //then
        assertThat(result).isNotEqualTo(preInviteCode);
    }

    @Test
    @DisplayName("그룹 초대코드 초기화 실패")
    void updateInviteCodeFailTest() {
        //given
        Group group = GroupBuilder.build(1L);

        given(groupRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> groupService.updateInviteCode(group.getId()))
                .isInstanceOf(NoExistGroupException.class);
    }

    @Test
    @DisplayName("그룹의 멤버 조회")
    void getMembersTest() {
        //given
        Page<MemberGroupDto> members = new PageImpl<>(
                new ArrayList<>(10),
                PageRequest.of(0,10),
                10L);

        given(memberRepository.searchMembersByGroupId(any(),any(),any())).willReturn(members);

        //when
        Page<MemberGroupDto> result = groupService
                .getMembers(1L,
                        new SearchGroupMemberDto("searchName"),
                        PageRequest.of(0, 10));

        //then
        assertThat(result.getTotalElements()).isEqualTo(10);
    }
}