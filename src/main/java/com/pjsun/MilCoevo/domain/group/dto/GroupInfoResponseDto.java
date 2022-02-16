package com.pjsun.MilCoevo.domain.group.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GroupInfoResponseDto {

    private String inviteCode;
    private String groupName;
}
