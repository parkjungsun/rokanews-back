package com.pjsun.MilCoevo.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 상태
 */

@Getter
@AllArgsConstructor
public enum PresentStatus {

    WORKING("업무"),
    STEPOUT("자리비움"),
    MISSED("부재중");

    private String description;
}
