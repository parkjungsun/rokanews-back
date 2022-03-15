package com.pjsun.MilCoevo.domain.news.dto;

import com.pjsun.MilCoevo.domain.news.News;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsDto {

    private Long id;
    private String keyword;
    private String title;
    private String imageLink;
    private String link;
    private String pubDate;
    private String timeIndex;

    public News getNews(String keyword, String timeIndex) {
        return News.createNewsBuilder()
                .title(title).link(link).keyword(keyword)
                .pubDate(pubDate).published(dToLdt(pubDate)).timeIndex(timeIndex).build();
    }

    @QueryProjection
    public NewsDto(Long id, String keyword, String title, String imageLink,
                   String link, String pubDate, String timeIndex) {
        this.id = id;
        this.keyword = keyword;
        this.title = title;
        this.imageLink = imageLink;
        this.link = link;
        this.pubDate = pubDate;
        this.timeIndex = timeIndex;
    }

    private LocalDateTime dToLdt(String date) {
        Date now = new Date(date);
        return LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
    }
}