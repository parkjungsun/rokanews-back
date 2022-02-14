package com.pjsun.MilCoevo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcessStatus {

    APPROVED("승인"),
    REJECTED("반려"),
    SUGGESTED("기안"),
    WITHDRAW("회수");

    private String description;
}
