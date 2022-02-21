package com.pjsun.MilCoevo.domain.purchase.service;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.member.service.MemberServiceImpl;
import com.pjsun.MilCoevo.domain.purchase.Item;
import com.pjsun.MilCoevo.domain.purchase.Purchase;
import com.pjsun.MilCoevo.domain.purchase.Purpose;
import com.pjsun.MilCoevo.domain.purchase.dto.ItemDto;
import com.pjsun.MilCoevo.domain.purchase.dto.PurchaseResponseDto;
import com.pjsun.MilCoevo.domain.purchase.dto.PurchasesResponseDto;
import com.pjsun.MilCoevo.domain.purchase.dto.SearchPurchaseDto;
import com.pjsun.MilCoevo.domain.purchase.repository.ItemRepository;
import com.pjsun.MilCoevo.domain.purchase.repository.PurchaseRepository;
import com.pjsun.MilCoevo.exception.NoExistGroupException;
import com.pjsun.MilCoevo.exception.NoPermissionException;
import com.pjsun.MilCoevo.exception.NoPurchaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseServiceImpl implements PurchaseService {

    private final MemberService memberService;
    private final GroupRepository groupRepository;
    private final ItemRepository itemRepository;
    private final PurchaseRepository purchaseRepository;

    @Transactional
    public Long addPurchase(Long groupId, String title, String content,
                            Purpose purpose, LocalDate purchaseDate, List<ItemDto> items) {

        Member member = memberService.getMemberByUserAndGroup(groupId);
        Group group = groupRepository.findById(groupId).orElseThrow(NoExistGroupException::new);

        Purchase purchase = Purchase.createPurchaseBuilder()
                .title(title).content(content).purpose(purpose)
                .drafter(member.sign()).group(group).purchaseDate(purchaseDate)
                .build();

        purchaseRepository.save(purchase);

        items.forEach(itemDto -> {
            Item item = Item.createItemBuilder()
                    .itemName(itemDto.getItemName())
                    .price(itemDto.getPrice())
                    .quantity(itemDto.getQuantity())
                    .purchase(purchase)
                    .build();
            itemRepository.save(item);
        });

        return purchase.getId();
    }

    public PurchaseResponseDto getPurchase(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(NoPurchaseException::new);

        return new PurchaseResponseDto(purchase);
    }

    public PurchasesResponseDto getPurchases(
            Long groupId, SearchPurchaseDto searchCondition, Pageable pageable) {

        Page<PurchaseResponseDto> purchases = purchaseRepository.searchPurchases(groupId, searchCondition, pageable);
        PurchasesResponseDto purchaseStatistics = purchaseRepository.searchPurchaseStatistics(groupId, searchCondition);

        purchaseStatistics.setPurchases(purchases);

        return purchaseStatistics;
    }

    @Transactional
    public void updatePurchase(Long groupId, Long purchaseId, ProcessStatus processStatus) {
        Member member = memberService.getMemberByUserAndGroup(groupId);

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(NoPurchaseException::new);

        if (havePermission(member, purchase)) {
            purchase.decide(member.sign(), processStatus);
        } else {
            throw new NoPermissionException();
        }
    }

    private boolean havePermission(Member member, Purchase purchase) {
        if(member.getRank().equals(Rank.LEADER)) {
            return true;
        }
        return purchase.getDrafter().getEmail().equals(member.getInfo().getEmail());
    }
}
