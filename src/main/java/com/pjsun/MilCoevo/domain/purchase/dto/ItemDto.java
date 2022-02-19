package com.pjsun.MilCoevo.domain.purchase.dto;

import com.pjsun.MilCoevo.domain.purchase.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemDto {

    @NotNull
    @Size(max = 50)
    private String itemName;

    @NotNull
    private Long price;

    @NotNull
    private Long quantity;

    public ItemDto(Item item) {
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
    }
}
