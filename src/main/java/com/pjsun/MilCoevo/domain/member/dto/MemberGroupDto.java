package com.pjsun.MilCoevo.domain.member.dto;

import com.pjsun.MilCoevo.domain.member.Rank;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberGroupDto {

    private Long id;
    private Long groupId;
    private String groupName;
    private Rank rank;
    private String email;
    private String position;
    private String nickname;
    private LocalDateTime lastVisitedDate;

    @QueryProjection
    public MemberGroupDto(
            Long id,
            Long groupId, String groupName, Rank rank, String email,
            String position, String nickname, LocalDateTime lastVisitedDate) {

        this.id = id;
        this.groupId =groupId;
        this.groupName = groupName;
        this.rank = rank;
        this.email = email;
        this.position = position;
        this.nickname = nickname;
        this.lastVisitedDate = lastVisitedDate;
    }
}
