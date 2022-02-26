package com.pjsun.MilCoevo.domain.member.service;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.repository.MemberRepository;
import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.domain.user.service.UserService;
import com.pjsun.MilCoevo.exception.NoExistGroupException;
import com.pjsun.MilCoevo.exception.NoMemberException;
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

class MemberServiceImplTest extends MockTest {

    @InjectMocks
    MemberServiceImpl memberService;

    @Mock
    UserService userService;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("유저의 멤버 불러오기")
    void getMemberByUserTest() {
        //given
        User user = UserBuilder.build(1L);

        Page<MemberGroupDto> members = new PageImpl<>(
                new ArrayList<>(10),
                PageRequest.of(0,10),
                10L);

        given(userService.getUserFromContext()).willReturn(user);
        given(memberRepository.searchMembersByUserId(any(),any())).willReturn(members);

        //when
        Page<MemberGroupDto> result = memberService
                .getMembersByUser(PageRequest.of(0, 10));

        //then
        assertThat(result.getTotalElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("멤버 가져오기")
    void getMemberByUserAndGroupTest() {
        //given
        User user = UserBuilder.build(1L);
        Member member = MemberBuilder.build(1L);

        given(userService.getUserFromContext()).willReturn(user);
        given(memberRepository.searchMemberByUserIdAndGroupId(any(),any()))
                .willReturn(Optional.of(member));

        //when
        Member result = memberService.getMemberByUserAndGroup(1L);

        //then
        assertThat(result.getInfo()).isEqualTo(member.getInfo());
    }

    @Test
    @DisplayName("멤버 가져오기 실패 No Group")
    void getMemberByUserAndGroupFailTest() {
        //given
        User user = UserBuilder.build(1L);

        given(userService.getUserFromContext()).willReturn(user);
        given(memberRepository.searchMemberByUserIdAndGroupId(any(),any()))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> memberService.getMemberByUserAndGroup(1L))
                .isInstanceOf(NoExistGroupException.class);
    }

    @Test
    @DisplayName("멤버 업데이트, 삭제")
    void updateRemoveMemberTest() {
        //given
        User user = UserBuilder.build(1L);
        Member member = MemberBuilder.build(1L);

        given(userService.getUserFromContext()).willReturn(user);
        given(memberRepository.searchMemberByUserIdAndGroupId(any(),any()))
                .willReturn(Optional.of(member));

        //when
        Long id = memberService.updateMember(1L, "update", "update");
        memberService.removeMember(1L);
    }

    @Test
    @DisplayName("멤버 업데이트 Rank, 삭제")
    void updateBanMemberRank() {
        //given
        Group group = GroupBuilder.build(1L);
        Member member = MemberBuilder.build(1L);
        member.setGroup(group);

        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        //when
        memberService.updateMemberRank(1L, member.getId(), Rank.LEADER);
        memberService.banMember(1L, member.getId());
    }

    @Test
    @DisplayName("멤버 업데이트 Rank, 삭제 실패 No Member")
    void updateBanMemberFailRank() {
        //given
        Group group = GroupBuilder.build(1L);
        Member member = MemberBuilder.build(1L);
        member.setGroup(group);

        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        //when
        assertThatThrownBy(() -> memberService.updateMemberRank(2L, member.getId(), Rank.LEADER))
                .isInstanceOf(NoMemberException.class);
    }
}