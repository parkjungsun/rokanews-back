package com.pjsun.MilCoevo.domain.group.dto;

import com.pjsun.MilCoevo.domain.member.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRankDto {
    @NotNull
    private Rank rank;
}
