package com.pjsun.MilCoevo.domain.member.repository;

import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    Page<MemberGroupDto> searchMembersByUserId(Long userId, Pageable pageable);
    Page<MemberGroupDto> searchMembersByGroupId(Long groupId, Pageable pageable);
    Optional<Member> searchMemberByUserIdAndGroupId(Long userId, Long groupId);
}
