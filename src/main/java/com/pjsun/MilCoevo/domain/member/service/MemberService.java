package com.pjsun.MilCoevo.domain.member.service;

import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.dto.MemberResponseDto;
import com.pjsun.MilCoevo.domain.member.repository.MemberRepository;
import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.domain.user.service.UserService;
import com.pjsun.MilCoevo.exception.InactiveGroupException;
import com.pjsun.MilCoevo.exception.InvalidTokenException;
import com.pjsun.MilCoevo.exception.NoExistGroupException;
import com.pjsun.MilCoevo.exception.NoMemberException;
import com.pjsun.MilCoevo.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final UserService userService;
    private final MemberRepository memberRepository;

    @Transactional
    public Page<MemberGroupDto> getMembersByUser(Pageable pageable) throws InvalidTokenException {
        User user = userService.getUserFromContext();

        return memberRepository.searchMembersByUserId(user.getId(), pageable);
    }

    public Member getMemberByUserAndGroup(Long groupId) throws InvalidTokenException {
        User user = userService.getUserFromContext();

        return memberRepository.searchMemberByUserIdAndGroupId(user.getId(), groupId)
                .orElseThrow(NoExistGroupException::new);
    }

    @Transactional
    public Long updateMember(Long groupId, String position, String nickname) throws InvalidTokenException {
        Member member = getMemberByUserAndGroup(groupId);

        member.updatePositionAndNickname(position, nickname);

        return member.getId();
    }

    @Transactional
    public void removeMember(Long groupId) throws InvalidTokenException {
        Member member = getMemberByUserAndGroup(groupId);

        member.changeAvailability(false);
    }

    @Transactional
    public Long updateMemberRank(Long memberId, Rank rank) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoMemberException::new);

        member.updateRank(rank);

        return member.getId();
    }

    @Transactional
    public void banMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoMemberException::new);

        member.changeAvailability(false);
    }
}
