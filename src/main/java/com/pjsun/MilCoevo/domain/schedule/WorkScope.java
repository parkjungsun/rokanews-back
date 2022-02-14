package com.pjsun.MilCoevo.domain.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkScope {

    // 종일, 시간
    CONTACT("대면"),
    UNTACT("비대면");

    private String description;
}
