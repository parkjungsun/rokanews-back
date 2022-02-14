package com.pjsun.MilCoevo.domain.member.repository;

import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    List<MemberGroupDto> searchMembersByUserId(Long userId);
    Optional<Member> searchMemberByUserIdAndGroupId(Long userId, Long groupId);
}
