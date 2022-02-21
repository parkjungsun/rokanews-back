package com.pjsun.MilCoevo.domain.member.service;

import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    Page<MemberGroupDto> getMembersByUser(Pageable pageable);

    Member getMemberByUserAndGroup(Long groupId);

    Long updateMember(Long groupId, String position, String nickname);

    Long updateMemberRank(Long memberId, Rank rank);

    void removeMember(Long groupId);

    void banMember(Long memberId);

}
