package com.pjsun.MilCoevo.domain.notice.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoticeAddRequestDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @NotNull
    @Size(max = 4096)
    private String content;
}
