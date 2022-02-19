package com.pjsun.MilCoevo.domain.group.service;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.dto.GroupInfoResponseDto;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.repository.MemberRepository;
import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.domain.user.service.UserService;
import com.pjsun.MilCoevo.exception.InactiveGroupException;
import com.pjsun.MilCoevo.exception.MaxMemberException;
import com.pjsun.MilCoevo.exception.NoExistGroupException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final UserService userService;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    public Group getGroupByInviteCode(String inviteCode) throws NoExistGroupException, InactiveGroupException {
        Group group = groupRepository
                   .findGroupByInviteCode(inviteCode)
                   .orElseThrow(NoExistGroupException::new);

        if(!group.isAvailable()) {
            throw new InactiveGroupException();
        }

        return group;
    }

    @Transactional
    public Long registerGroup(String inviteCode, String position, String nickname) throws MaxMemberException, NoExistGroupException, InactiveGroupException {
        User user = userService.getUserFromContext();
        if (user.getMembers().size() > 5) {
            throw new MaxMemberException();
        }
        Group group = getGroupByInviteCode(inviteCode);

        Identification info = Identification.createIdentificationBuilder()
                .email(user.getEmail()).position(position)
                .nickname(nickname).build();

        Member member = memberRepository.searchMemberByUserIdAndGroupId(user.getId(), group.getId()).orElse(null);
        if(member == null) {
            log.debug("create new member");
            member = Member.createToMemberBuilder().info(info).user(user).group(group).build();
            memberRepository.save(member);
        }
        member.changeAvailability(true);

        return member.getId();
    }

    @Transactional
    public Long createGroup(String groupName, String position, String nickname) {
        User user = userService.getUserFromContext();
        Group group = Group.createGroupBuilder(groupName);

        Identification info = Identification.createIdentificationBuilder()
                .email(user.getEmail()).position(position)
                .nickname(nickname).build();

        Member member = Member.createToLeaderBuilder()
                .info(info).user(user).group(group).build();

        groupRepository.save(group);
        memberRepository.save(member);

        return group.getId();
    }

    public GroupInfoResponseDto getGroupInfo(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(NoExistGroupException::new);
        return new GroupInfoResponseDto(group.getInviteCode(), group.getGroupName());
    }

    @Transactional
    public String updateGroupName(Long groupId, String groupName) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(NoExistGroupException::new);
        group.updateGroupName(groupName);
        return group.getGroupName();
    }

    @Transactional
    public String updateInviteCode(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(NoExistGroupException::new);
        group.updateInviteCode();
        return group.getInviteCode();
    }

    public Page<MemberGroupDto> getMembers(Long groupId, Pageable pageable) {
        return memberRepository.searchMembersByGroupId(groupId, pageable);
    }

}
