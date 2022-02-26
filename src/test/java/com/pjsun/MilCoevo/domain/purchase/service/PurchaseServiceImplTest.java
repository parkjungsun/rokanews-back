package com.pjsun.MilCoevo.domain.purchase.service;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.PresentStatus;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.purchase.Item;
import com.pjsun.MilCoevo.domain.purchase.Purchase;
import com.pjsun.MilCoevo.domain.purchase.dto.PurchaseResponseDto;
import com.pjsun.MilCoevo.domain.purchase.dto.PurchasesResponseDto;
import com.pjsun.MilCoevo.domain.purchase.dto.SearchPurchaseDto;
import com.pjsun.MilCoevo.domain.purchase.repository.ItemRepository;
import com.pjsun.MilCoevo.domain.purchase.repository.PurchaseRepository;
import com.pjsun.MilCoevo.exception.NoPermissionException;
import com.pjsun.MilCoevo.exception.NoPurchaseException;
import com.pjsun.MilCoevo.test.MockTest;
import com.pjsun.MilCoevo.test.builder.GroupBuilder;
import com.pjsun.MilCoevo.test.builder.MemberBuilder;
import com.pjsun.MilCoevo.test.builder.PurchaseBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class PurchaseServiceImplTest extends MockTest {

    @InjectMocks
    PurchaseServiceImpl purchaseService;

    @Mock
    MemberService memberService;

    @Mock
    GroupRepository groupRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    PurchaseRepository purchaseRepository;

    @Test
    @DisplayName("그룹 구매 추가")
    void addPurchaseTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Group group = GroupBuilder.build(1L);
        Purchase purchase = PurchaseBuilder.build(1L);
        Item item = Item.createItemBuilder()
                .itemName("item").quantity(1L).price(1000L).build();

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(groupRepository.findById(any())).willReturn(Optional.of(group));
        given(purchaseRepository.save(any())).willReturn(purchase);

        //when
        Long id = purchaseService.addPurchase(group.getId(), purchase.getTitle(),
                purchase.getContent(), purchase.getPurpose(), purchase.getPurchaseDate(), new ArrayList<>());
    }

    @Test
    @DisplayName("구매 조회")
    void getPurchaseTest() {
        //given
        Purchase purchase = PurchaseBuilder.build(1L);

        given(purchaseRepository.findById(any())).willReturn(Optional.of(purchase));

        //when
        PurchaseResponseDto result = purchaseService.getPurchase(purchase.getId());

        //then
        assertThat(result.getId()).isEqualTo(purchase.getId());
        assertThat(result.getTitle()).isEqualTo(purchase.getTitle());
        assertThat(result.getContent()).isEqualTo(purchase.getContent());
        assertThat(result.getPurpose()).isEqualTo(purchase.getPurpose());
        assertThat(result.getPurchasePrice()).isEqualTo(purchase.getPurchasePrice());
        assertThat(result.getPurchaseDate()).isEqualTo(purchase.getPurchaseDate());
        assertThat(result.getDrafterEmail()).isEqualTo(purchase.getDrafter().getEmail());
        assertThat(result.getDrafterPosition()).isEqualTo(purchase.getDrafter().getPosition());
        assertThat(result.getDrafterNickname()).isEqualTo(purchase.getDrafter().getNickname());
    }

    @Test
    @DisplayName("구매 조회 실패")
    void getPurchaseFailTest() {
        //given
        Purchase purchase = PurchaseBuilder.build(1L);

        given(purchaseRepository.findById(any())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> purchaseService.getPurchase(purchase.getId()))
                .isInstanceOf(NoPurchaseException.class);
    }

    @Test
    @DisplayName("그룹 구매 조회")
    void getPurchasesTest() {
        //given
        Page<PurchaseResponseDto> purchases = new PageImpl<>(
                new ArrayList<>(10),
                PageRequest.of(0,10),
                10L);
        PurchasesResponseDto purchaseStatistics = new PurchasesResponseDto(0L, 0L, 0L, 0L, 0L, null);

        given(purchaseRepository.searchPurchases(any(),any(),any())).willReturn(purchases);
        given(purchaseRepository.searchPurchaseStatistics(any(), any())).willReturn(purchaseStatistics);

        //when
        PurchasesResponseDto result = purchaseService.getPurchases(1L,
                new SearchPurchaseDto(
                        LocalDate.of(2022, 2, 10),
                        LocalDate.of(2022, 2, 20), null),
                PageRequest.of(0, 10));

        //then
        assertThat(result.getPurchases().getTotalElements()).isEqualTo(10);
        assertThat(result.getTotal()).isEqualTo(0L);
        assertThat(result.getOffice()).isEqualTo(0L);
        assertThat(result.getLecture()).isEqualTo(0L);
        assertThat(result.getTravel()).isEqualTo(0L);
        assertThat(result.getEtc()).isEqualTo(0L);
    }

    @Test
    @DisplayName("구매 업데이트 성공")
    void updatePurchaseTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Purchase purchase = PurchaseBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(purchaseRepository.findById(any())).willReturn(Optional.of(purchase));

        //when
        purchaseService.updatePurchase(1L, purchase.getId(), purchase.getProcessStatus());
    }

    @Test
    @DisplayName("구매 업데이트 실패 No Purchase")
    void PurchaseServiceFailTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Purchase purchase = PurchaseBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(purchaseRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> purchaseService.updatePurchase(1L, purchase.getId(), purchase.getProcessStatus()))
                .isInstanceOf(NoPurchaseException.class);
    }

    @Test
    @DisplayName("구매 업데이트 실패 No Permission")
    void PurchaseServiceFail2Test() {
        //given
        Member member = new Member(1L, Identification.createIdentificationBuilder()
                .email("test").nickname("ni").position("po").build(), Rank.MEMBER,
                PresentStatus.MISSED, true, LocalDateTime.now(),
                null, null);
        Purchase purchase = PurchaseBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(purchaseRepository.findById(any())).willReturn(Optional.of(purchase));

        //then
        assertThatThrownBy(() -> purchaseService.updatePurchase(1L, purchase.getId(), purchase.getProcessStatus()))
                .isInstanceOf(NoPermissionException.class);
    }
}