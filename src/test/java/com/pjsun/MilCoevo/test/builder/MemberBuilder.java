package com.pjsun.MilCoevo.test.builder;

import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.PresentStatus;
import com.pjsun.MilCoevo.domain.member.Rank;

import java.time.LocalDateTime;

public class MemberBuilder {

    public static Member build(Long id) {
        return new Member(id, IdentificationBuilder.build(), Rank.MEMBER,
                PresentStatus.MISSED, true, LocalDateTime.now(),
                null, null);
    }
}
