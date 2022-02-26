package com.pjsun.MilCoevo.domain.purchase;

import com.pjsun.MilCoevo.test.DomainTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest extends DomainTest {

    @Test
    @DisplayName("물품 생성")
    void createItemTest() {
        //given
        String itemName = "test";
        Long price = 24000L;
        Long quantity = 4L;

        //when
        Item item = Item.createItemBuilder()
                .itemName(itemName).price(price)
                .quantity(quantity).build();

        //then
        assertThat(item.getItemName()).isEqualTo(itemName);
        assertThat(item.getPrice()).isEqualTo(price);
        assertThat(item.getQuantity()).isEqualTo(quantity);
        assertThat(item.getItemPrice()).isEqualTo(price * quantity);
    }
}