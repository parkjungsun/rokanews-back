package com.pjsun.MilCoevo.domain.group.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGroupNameRequestDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String groupName;
}
