package com.pjsun.MilCoevo.domain.purchase.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.pjsun.MilCoevo.domain.purchase.Purchase;
import com.pjsun.MilCoevo.domain.purchase.Purpose;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PurchaseResponseDto {

    private Long id;
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;
    private Purpose purpose;
    private Long purchasePrice;
    private LocalDate purchaseDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String drafterEmail;
    private String drafterPosition;
    private String drafterNickname;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String arbiterEmail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String arbiterPosition;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String arbiterNickname;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime decisionDate;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ItemDto> items;

    @QueryProjection
    public PurchaseResponseDto(
            Long id, String title, Purpose purpose, Long purchasePrice,
            String drafterPosition, String drafterNickname, LocalDate purchaseDate) {
        this.id = id;
        this.title = title;
        this.purpose = purpose;
        this.purchasePrice = purchasePrice;
        this.drafterPosition = drafterPosition;
        this.drafterNickname = drafterNickname;
        this.purchaseDate = purchaseDate;
    }

    public PurchaseResponseDto(Purchase purchase) {
        this.id = purchase.getId();
        this.title = purchase.getTitle();
        this.content = purchase.getContent();
        this.purpose = purchase.getPurpose();
        this.purchasePrice = purchase.getPurchasePrice();

        this.drafterEmail = purchase.getDrafter().getEmail();
        this.drafterPosition = purchase.getDrafter().getPosition();
        this.drafterNickname = purchase.getDrafter().getNickname();

        this.arbiterEmail = purchase.getArbiter().getEmail();
        this.arbiterPosition = purchase.getArbiter().getPosition();
        this.arbiterNickname = purchase.getArbiter().getNickname();

        this.createdDate = purchase.getCreatedDate();
        this.decisionDate = purchase.getDecisionDate();
        this.purchaseDate = purchase.getPurchaseDate();
        this.items = purchase.getItems().stream()
                .map(ItemDto::new).collect(Collectors.toList());
    }
}
