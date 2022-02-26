package com.pjsun.MilCoevo.test.builder;

import com.pjsun.MilCoevo.domain.news.Keyword;

public class KeywordBuilder {

    public static Keyword build(Long id) {
        return new Keyword(1L, "content", null);
    }
}
