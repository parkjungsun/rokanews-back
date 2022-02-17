package com.pjsun.MilCoevo.domain.member.service;

import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.dto.MemberResponseDto;
import com.pjsun.MilCoevo.domain.member.repository.MemberRepository;
import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.domain.user.service.UserService;
import com.pjsun.MilCoevo.exception.InvalidTokenException;
import com.pjsun.MilCoevo.exception.NoExistGroupException;
import com.pjsun.MilCoevo.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<MemberGroupDto> getMembersByUser() throws InvalidTokenException {
        User user = userService.getUserFromContext();

        return memberRepository.searchMembersByUserId(user.getId());
    }

    public MemberResponseDto getMemberByUserAndGroup(Long groupId) throws InvalidTokenException {
        User user = userService.getUserFromContext();

        Member member = memberRepository.searchMemberByUserIdAndGroupId(user.getId(), groupId)
                .orElseThrow(NoExistGroupException::new);

        return new MemberResponseDto(member);
    }
}
