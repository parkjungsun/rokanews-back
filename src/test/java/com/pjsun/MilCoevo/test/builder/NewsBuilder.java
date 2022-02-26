package com.pjsun.MilCoevo.test.builder;

import com.pjsun.MilCoevo.domain.news.News;

public class NewsBuilder {

    public static News build(Long id) {
        return new News(id, "keyword", "title",
                "imageLink", "link", "pubDate", "index");
    }
}
