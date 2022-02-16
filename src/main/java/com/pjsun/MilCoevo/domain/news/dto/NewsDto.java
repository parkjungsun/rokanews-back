package com.pjsun.MilCoevo.domain.news.dto;

import com.pjsun.MilCoevo.domain.news.News;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsDto {

    private Long id;
    private String keyword;
    private String title;
    private String imageLink;
    private String link;
    private String pubDate;
    private String index;

    public News getNews(String keyword, String index) {
        return News.createNewsBuilder()
                .title(title).link(link).keyword(keyword)
                .pubDate(pubDate).index(index).build();
    }

    @QueryProjection
    public NewsDto(Long id, String keyword, String title, String imageLink,
                   String link, String pubDate, String index) {
        this.id = id;
        this.keyword = keyword;
        this.title = title;
        this.imageLink = imageLink;
        this.link = link;
        this.pubDate = pubDate;
        this.index = index;
    }
}