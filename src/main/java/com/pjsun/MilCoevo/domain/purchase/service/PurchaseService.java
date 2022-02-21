package com.pjsun.MilCoevo.domain.purchase.service;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.purchase.Purpose;
import com.pjsun.MilCoevo.domain.purchase.dto.ItemDto;
import com.pjsun.MilCoevo.domain.purchase.dto.PurchaseResponseDto;
import com.pjsun.MilCoevo.domain.purchase.dto.PurchasesResponseDto;
import com.pjsun.MilCoevo.domain.purchase.dto.SearchPurchaseDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseService {

    PurchasesResponseDto getPurchases(Long groupId,
                                      SearchPurchaseDto searchCondition,
                                      Pageable pageable);

    PurchaseResponseDto getPurchase(Long purchaseId);

    Long addPurchase(Long groupId, String title, String content,
                     Purpose purpose, LocalDate purchaseDate,
                     List<ItemDto> items);

    void updatePurchase(Long groupId, Long purchaseId,
                        ProcessStatus processStatus);

}
