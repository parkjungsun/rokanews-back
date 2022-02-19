package com.pjsun.MilCoevo.domain.purchase;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Slf4j
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "items")
@ToString(exclude = {"purchase"})
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String itemName;

    private Long price;

    private Long quantity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    /* 연관관계 메서드 */
    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
        purchase.getItems().add(this);
        purchase.addPurchasePrice(getItemPrice());
    }

    /* 생성자 */
    @Builder(builderClassName = "of", builderMethodName = "of")
    private Item(String itemName, Long price, Long quantity, Purchase purchase) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;

        if(!ObjectUtils.isEmpty(purchase)) {
            setPurchase(purchase);
        }
    }

    @Builder(builderClassName = "createItemBuilder", builderMethodName = "createItemBuilder")
    public static Item createItem(String itemName, Long price, Long quantity, Purchase purchase) {
        Assert.hasText(itemName, () -> "[Item] itemName must not be empty");
        Assert.notNull(price, () -> "[Item] price must not be null");
        Assert.notNull(quantity, () -> "[Item] quantity must not be null");

        return Item.of()
                .itemName(itemName).price(price)
                .quantity(quantity).purchase(purchase)
                .build();
    }

    /* 비즈니스 로직 */
    public Long getItemPrice() {
        return price * quantity;
    }

}
