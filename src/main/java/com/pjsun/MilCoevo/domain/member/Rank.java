package com.pjsun.MilCoevo.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rank {

    LEADER("탐장"),
    MEMBER("팀원"),
    OTHERS("미승인");

    private String description;
}
