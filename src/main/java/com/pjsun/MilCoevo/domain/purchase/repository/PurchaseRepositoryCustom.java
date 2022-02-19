package com.pjsun.MilCoevo.domain.purchase.repository;

import com.pjsun.MilCoevo.domain.absence.dto.SearchAbsenceDto;
import com.pjsun.MilCoevo.domain.purchase.dto.PurchaseResponseDto;
import com.pjsun.MilCoevo.domain.purchase.dto.PurchasesResponseDto;
import com.pjsun.MilCoevo.domain.purchase.dto.SearchPurchaseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseRepositoryCustom {

    Page<PurchaseResponseDto> searchPurchases(Long groupId, SearchPurchaseDto searchCondition, Pageable pageable);
    PurchasesResponseDto searchPurchaseStatistics(Long groupId, SearchPurchaseDto searchCondition);

}
