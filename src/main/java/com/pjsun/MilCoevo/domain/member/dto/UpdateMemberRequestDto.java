package com.pjsun.MilCoevo.domain.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateMemberRequestDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String position;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;
}
