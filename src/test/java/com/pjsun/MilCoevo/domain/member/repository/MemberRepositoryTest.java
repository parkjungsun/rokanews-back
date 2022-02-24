package com.pjsun.MilCoevo.domain.member.repository;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.dto.SearchGroupMemberDto;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    Group group;
    User user;

    @BeforeEach
    void init() {
        user = User.createByUser("testUser", "password");
        userRepository.save(user);

        group = Group.createGroupBuilder("testGroup");
        groupRepository.save(group);

        Identification info = Identification.createIdentificationBuilder()
                .email("test@test.com").nickname("nickname")
                .position("position").build();

        Member member = Member.createToMemberBuilder()
                .info(info).user(user).group(group).build();

        memberRepository.save(member);
    }

    @Test
    @DisplayName("유저의 멤버 가져오기")
    void searchMembersByUserIdTest() {
        //given
        Pageable pageable = PageRequest.of(0,10);

        //when
        Page<MemberGroupDto> members = memberRepository
                .searchMembersByUserId(user.getId(), pageable);

        //then
        Assertions.assertThat(members.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(members.getSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("그룹의 멤버 가져오기")
    void searchMembersByGroupIdTest() {
        //given
        SearchGroupMemberDto searchCondition =
                new SearchGroupMemberDto("nick");
        Pageable pageable = PageRequest.of(0,10);

        //when
        Page<MemberGroupDto> members = memberRepository
                .searchMembersByGroupId(group.getId(), searchCondition, pageable);

        //then
        Assertions.assertThat(members.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(members.getSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("유저, 그룹의 멤버 가저오기")
    void searchMemberByUserIdAndGroupIdTest() {
        //when
        Optional<Member> member = memberRepository
                .searchMemberByUserIdAndGroupId(user.getId(), group.getId());

        //then
        Assertions.assertThat(member.orElse(null)).isNotNull();
    }
}