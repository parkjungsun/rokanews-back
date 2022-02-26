package com.pjsun.MilCoevo.domain.purchase;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.test.DomainTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PurchaseTest extends DomainTest {

    @Test
    @DisplayName("구매 생성")
    void createPurchaseTest() {
        //given
        String title = "title";
        String content = "content";
        Purpose purpose = Purpose.ETC_PURPOSE;
        Identification info = Identification.createIdentificationBuilder()
                .email("test@test.com").position("position")
                .nickname("nickname").build();
        LocalDate purchaseDate = LocalDate.of(2022,2,20);

        //when
        Purchase purchase = Purchase.createPurchaseBuilder()
                .title(title).content(content).purpose(purpose)
                .drafter(info).purchaseDate(purchaseDate).build();

        //then
        assertThat(purchase.getTitle()).isEqualTo(title);
        assertThat(purchase.getContent()).isEqualTo(content);
        assertThat(purchase.getPurpose()).isEqualTo(purpose);
        assertThat(purchase.getDrafter()).isEqualTo(info);
        assertThat(purchase.getPurchaseDate()).isEqualTo(purchaseDate);

        assertThat(purchase.getPurchasePrice()).isEqualTo(0L);
        assertThat(purchase.getProcessStatus()).isEqualTo(ProcessStatus.SUGGESTED);
    }

    @Test
    @DisplayName("비즈니스 로직")
    void businessTest() {
        //given
        String title = "title";
        String content = "content";
        Purpose purpose = Purpose.ETC_PURPOSE;
        Identification info = Identification.createIdentificationBuilder()
                .email("test@test.com").position("position")
                .nickname("nickname").build();
        LocalDate purchaseDate = LocalDate.of(2022,2,20);

        //when
        Purchase purchase = Purchase.createPurchaseBuilder()
                .title(title).content(content).purpose(purpose)
                .drafter(info).purchaseDate(purchaseDate).build();

        purchase.addPurchasePrice(1300L);
        purchase.addPurchasePrice(2500L);
        purchase.decide(info, ProcessStatus.APPROVED);

        //then
        assertThat(purchase.getPurchasePrice()).isEqualTo(3800L);
        assertThat(purchase.getProcessStatus()).isEqualTo(ProcessStatus.APPROVED);
    }
}