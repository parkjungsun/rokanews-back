package com.pjsun.MilCoevo.domain.purchase;

import com.pjsun.MilCoevo.domain.BaseEntity;
import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.member.Identification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Slf4j
@Entity @Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "purchase")
@ToString(exclude = {"group", "items"})
public class Purchase extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "purchase_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 4096)
    private String content;

    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @Enumerated(EnumType.STRING)
    private ProcessStatus processStatus;

    private Long purchasePrice;

    private LocalDate purchaseDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "drafter_email", nullable = false)),
            @AttributeOverride(name = "position", column = @Column(name = "drafter_position", nullable = false)),
            @AttributeOverride(name = "nickname", column = @Column(name = "drafter_nickname", nullable = false))
    })
    private Identification drafter;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "arbiter_email")),
            @AttributeOverride(name = "position", column = @Column(name = "arbiter_position")),
            @AttributeOverride(name = "nickname", column = @Column(name = "arbiter_nickname"))
    })
    private Identification arbiter;

    private LocalDateTime decisionDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    /* 연관관계 메서드 */
    public void setGroup(Group group) {
        this.group = group;
        group.getPurchases().add(this);
    }

    public void addItem(Item item) {
        this.items.add(item);
        item.setPurchase(this);

        addPurchasePrice(item.getItemPrice());
    }

    /* 생성자 */
    @Builder(builderClassName = "of", builderMethodName = "of")
    private Purchase(String title, String content, Purpose purpose, Long purchasePrice,
                     ProcessStatus processStatus, Identification drafter, Group group,
                     LocalDate purchaseDate) {
        this.title = title;
        this.content = content;
        this.purpose = purpose;
        this.purchasePrice = purchasePrice;
        this.processStatus = processStatus;
        this.drafter = drafter;
        this.purchaseDate = purchaseDate;

        if(!ObjectUtils.isEmpty(group)) {
            setGroup(group);
        }
    }

    @Builder(builderClassName = "createPurchaseBuilder", builderMethodName = "createPurchaseBuilder")
    public static Purchase createPurchase(String title, String content, Purpose purpose,
                                          Identification drafter, Group group, LocalDate purchaseDate) {
        Assert.hasText(title, () -> "[Purchase] title must not be empty");
        Assert.notNull(content, () -> "[Purchase] content must not be null");
        Assert.notNull(purpose, () -> "[Purchase] purpose must not be null");
        Assert.notNull(drafter, () -> "[Purchase] drafter must not be null");
        Assert.notNull(purchaseDate, () -> "[Purchase] purchaseDate must not be null");

        return Purchase.of()
                .title(title).content(content)
                .purpose(purpose).purchasePrice(0L)
                .processStatus(ProcessStatus.SUGGESTED)
                .drafter(drafter).group(group)
                .purchaseDate(purchaseDate)
                .build();
    }

    /* 비즈니스 로직 */
    public void addPurchasePrice(Long itemPrice) {
        this.purchasePrice += itemPrice;
    }

    public void decide(Identification sign, ProcessStatus processStatus) {
        Assert.notNull(sign, () -> "[Purchase] sign must not be null");
        Assert.notNull(processStatus, () -> "[Purchase] processStatus must not be null");

        this.arbiter = sign;
        this.processStatus = processStatus;
        this.decisionDate = LocalDateTime.now();
    }

}
