package com.pjsun.MilCoevo.domain.news;

import com.pjsun.MilCoevo.domain.BaseEntity;
import com.pjsun.MilCoevo.domain.group.Group;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Slf4j
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "keywords")
@ToString(exclude = {"group"})
public class Keyword extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "keyword_id")
    private Long id;

    @Column(length = 50)
    private String content;

    @ManyToOne(fetch = LAZY)
    private Group group;

    /* 연관관계 메서드 */
    public void setGroup(Group group) {
        this.group = group;
        group.getKeywords().add(this);
    }

    /* 생성자 */
    @Builder(builderClassName = "of", builderMethodName = "of")
    private Keyword(String content, Group group) {
        this.content = content;

        if(!ObjectUtils.isEmpty(group)) {
            setGroup(group);
        }
    }

    public static Keyword createKeyword(String content, Group group) {
        Assert.hasText(content, () -> "[Keyword] content must not be empty");

        return Keyword.of()
                .content(content).group(group)
                .build();
    }

    /* 비즈니스 로직 */

}

