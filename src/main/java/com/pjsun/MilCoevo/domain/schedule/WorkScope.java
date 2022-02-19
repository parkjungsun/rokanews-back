package com.pjsun.MilCoevo.domain.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkScope {

    // 종일, 시간
    ALL_DAY("종일"),
    PART_TIME("시간");

    private final String description;
}
