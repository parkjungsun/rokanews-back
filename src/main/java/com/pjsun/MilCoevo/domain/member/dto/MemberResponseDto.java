package com.pjsun.MilCoevo.domain.member.dto;

import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.Rank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberResponseDto {

    private String email;
    private String position;
    private String nickname;
    private Rank rank;

    public MemberResponseDto(Member member) {
        this.email = member.getInfo().getEmail();
        this.position = member.getInfo().getPosition();
        this.nickname = member.getInfo().getNickname();
        this.rank = member.getRank();
    }
}
