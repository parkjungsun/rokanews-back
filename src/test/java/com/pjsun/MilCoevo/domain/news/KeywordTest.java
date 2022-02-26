package com.pjsun.MilCoevo.domain.news;

import com.pjsun.MilCoevo.test.DomainTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KeywordTest extends DomainTest {

    @Test
    @DisplayName("키워드 생성")
    void createKeywordTest() {
        //given
        String content = "test";

        //when
        Keyword keyword = Keyword.createKeywordBuilder()
                .content(content).build();

        //then
        assertThat(keyword.getContent()).isEqualTo(content);
    }
}