package com.pjsun.MilCoevo.domain.purchase.dto;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PurchaseUpdateRequestDto {

    @NotNull
    private ProcessStatus processStatus;
}
