package com.pjsun.MilCoevo.domain.news;

import com.pjsun.MilCoevo.test.DomainTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

class NewsTest extends DomainTest {

    @Test
    @DisplayName("뉴스 생성")
    void createNewsTest() {
        //given
        String keyword = "test6";
        String title = "test5";
        String imageLink = "test4";
        String link = "test3";
        String pubDate = "test2";
        String index = "test1";

        //when
        News news = News.createNewsBuilder()
                .keyword(keyword).title(title).imageLink(imageLink)
                .link(link).pubDate(pubDate).index(index).build();

        //then
        assertThat(news.getKeyword()).isEqualTo(keyword);
        assertThat(news.getTitle()).isEqualTo(title);
        assertThat(news.getImageLink()).isEqualTo(imageLink);
        assertThat(news.getLink()).isEqualTo(link);
        assertThat(news.getPubDate()).isEqualTo(pubDate);
        assertThat(news.getIndex()).isEqualTo(index);
    }
}