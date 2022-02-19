package com.pjsun.MilCoevo.domain.purchase.dto;

import com.pjsun.MilCoevo.domain.purchase.Purpose;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PurchaseAddRequestDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @Size(max = 4096)
    private String content;

    @NotNull
    private Purpose purpose;

    @NotNull
    private LocalDate purchaseDate;

    private List<ItemDto> items;
}
