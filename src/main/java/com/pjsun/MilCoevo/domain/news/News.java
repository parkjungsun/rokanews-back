package com.pjsun.MilCoevo.domain.news;

import com.pjsun.MilCoevo.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Slf4j
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "news")
public class News extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "news_idd")
    private Long id;

    @Column(length = 50)
    private String keyword;

    private String title;

    private String imageLink;

    private String link;

    private LocalDateTime pubDate;

    /* 생성자 */
    @Builder(builderClassName = "createNewsBuilder", builderMethodName = "createNewsBuilder")
    News(String keyword, String title, String imageLink, String link, LocalDateTime pubDate) {
        Assert.hasText(keyword, () -> "[News] keyword must not be empty");
        Assert.hasText(title, () -> "[News] title must not be empty");
        Assert.hasText(imageLink, () -> "[News] imageLink must not be empty");
        Assert.hasText(link, () -> "[News] link must not be empty");
        Assert.notNull(pubDate, () -> "[News] pubDate must not be empty");

        this.keyword = keyword;
        this.title = title;
        this.imageLink = imageLink;
        this.link = link;
        this.pubDate = pubDate;
    }

    /* 비즈니스 로직 */

}
