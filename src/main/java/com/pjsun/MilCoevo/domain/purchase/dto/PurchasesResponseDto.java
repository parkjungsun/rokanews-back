package com.pjsun.MilCoevo.domain.purchase.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PurchasesResponseDto {

    private Long total;
    private Long office;
    private Long lecture;
    private Long travel;
    private Long etc;
    private Page<PurchaseResponseDto> purchases;

    public void setPurchases(Page<PurchaseResponseDto> purchases) {
        this.purchases = purchases;
    }

    @QueryProjection
    public PurchasesResponseDto(
            Long total, Long office, Long lecture, Long travel, Long etc) {
        this.total = total;
        this.office = office;
        this.lecture = lecture;
        this.travel = travel;
        this.etc = etc;
    }
}
