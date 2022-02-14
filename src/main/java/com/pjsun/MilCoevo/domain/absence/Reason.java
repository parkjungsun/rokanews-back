package com.pjsun.MilCoevo.domain.absence;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Reason {

    ANNUAL("연가"),
    OFFICIAL("공가"),
    PETITION("청원"),
    BUSINESS("출장"),
    DISPATCH("파견"),
    EDUCATION("교육"),
    ETC_REASON("기타");

    private String description;
}
