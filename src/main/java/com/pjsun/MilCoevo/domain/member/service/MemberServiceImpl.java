package com.pjsun.MilCoevo.domain.member.service;

import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.repository.MemberRepository;
import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.domain.user.service.UserService;
import com.pjsun.MilCoevo.domain.user.service.UserServiceImpl;
import com.pjsun.MilCoevo.exception.InvalidTokenException;
import com.pjsun.MilCoevo.exception.NoExistGroupException;
import com.pjsun.MilCoevo.exception.NoMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final UserService userService;
    private final MemberRepository memberRepository;

    @Transactional
    public Page<MemberGroupDto> getMembersByUser(Pageable pageable) throws InvalidTokenException {
        User user = userService.getUserFromContext();

        return memberRepository.searchMembersByUserId(user.getId(), pageable);
    }

    public Member getMemberByUserAndGroup(Long groupId) throws InvalidTokenException {
        User user = userService.getUserFromContext();

        Member member = memberRepository.searchMemberByUserIdAndGroupId(user.getId(), groupId)
                .orElseThrow(NoExistGroupException::new);

        if(!member.isAvailable()) {
            throw new NoExistGroupException();
        }
        return member;
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
        member.updateRank(Rank.OTHERS);
    }

    @Transactional
    public Long updateMemberRank(Long groupId, Long memberId, Rank rank) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoMemberException::new);

        if(!member.getGroup().getId().equals(groupId)) {
            throw new NoMemberException();
        }

        member.updateRank(rank);

        return member.getId();
    }

    @Transactional
    public void banMember(Long groupId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoMemberException::new);

        if(!member.getGroup().getId().equals(groupId)) {
            throw new NoMemberException();
        }

        member.changeAvailability(false);
        member.updateRank(Rank.OTHERS);
    }

}
