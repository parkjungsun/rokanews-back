package com.pjsun.MilCoevo.domain.purchase.repository;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.domain.purchase.Item;
import com.pjsun.MilCoevo.domain.purchase.Purchase;
import com.pjsun.MilCoevo.domain.purchase.Purpose;
import com.pjsun.MilCoevo.domain.purchase.dto.PurchaseResponseDto;
import com.pjsun.MilCoevo.domain.purchase.dto.PurchasesResponseDto;
import com.pjsun.MilCoevo.domain.purchase.dto.SearchPurchaseDto;
import com.pjsun.MilCoevo.domain.schedule.dto.SearchScheduleDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PurchaseRepositoryTest {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    GroupRepository groupRepository;

    Group group;

    @BeforeEach
    void init()  {
        group = Group.createGroupBuilder("testGroup");
        groupRepository.save(group);

        Identification sign = Identification.createIdentificationBuilder()
                .email("schedule@test.com").nickname("schedule")
                .position("scheduler").build();

        for (int i = 1; i < 20; i ++) {
            Purchase purchase = Purchase.createPurchaseBuilder()
                    .title("test").content("content")
                    .purpose(Purpose.ETC_PURPOSE)
                    .purchaseDate(LocalDate.of(2022, 2, i))
                    .drafter(sign).group(group)
                    .build();
            purchaseRepository.save(purchase);

            for(int j = 1; j < 4; j++) {
                Item item = Item.createItemBuilder()
                        .itemName("testItem")
                        .price(i * 1000L)
                        .quantity((long) j)
                        .purchase(purchase)
                        .build();
                itemRepository.save(item);
            }
        }
    }

    @Test
    @DisplayName("구매 리스트 불러오기")
    void searchPurchasesTest()  {
        //given
        SearchPurchaseDto searchCondition = new SearchPurchaseDto(
                LocalDate.of(2022, 2, 10),
                LocalDate.of(2022,2,15),
                ProcessStatus.SUGGESTED);

        Pageable pageable = PageRequest.of(0,8);

        //when
        Page<PurchaseResponseDto> response = purchaseRepository
                .searchPurchases(group.getId(), searchCondition, pageable);

        //then
        assertThat(response.getContent().size()).isEqualTo(6);
        assertThat(response.getSize()).isEqualTo(8);
    }

    @Test
    @DisplayName("구매 통계")
    void searchPurchaseStatisticsTest() {
        //given
        SearchPurchaseDto searchCondition = new SearchPurchaseDto(
                LocalDate.of(2022, 2, 1),
                LocalDate.of(2022,2,2),
                ProcessStatus.SUGGESTED);

        //when
        PurchasesResponseDto purchaseStatistics = purchaseRepository
                .searchPurchaseStatistics(group.getId(), searchCondition);

        //then
        assertThat(purchaseStatistics.getTotal()).isEqualTo(18000);
        assertThat(purchaseStatistics.getEtc()).isEqualTo(18000);
    }
}