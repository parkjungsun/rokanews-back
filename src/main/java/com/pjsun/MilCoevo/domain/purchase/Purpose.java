package com.pjsun.MilCoevo.domain.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Purpose {

    OFFICE("사무용품"),
    LECTURE("교육비"),
    TRAVEL("출장비"),
    ETC_PURPOSE("기타");

    private String description;
}
