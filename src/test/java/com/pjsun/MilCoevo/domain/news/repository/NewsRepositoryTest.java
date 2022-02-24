package com.pjsun.MilCoevo.domain.news.repository;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.news.Keyword;
import com.pjsun.MilCoevo.domain.news.News;
import com.pjsun.MilCoevo.domain.news.dto.NewsDto;
import com.pjsun.MilCoevo.domain.news.dto.SearchNewsDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class NewsRepositoryTest {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    KeywordRepository keywordRepository;

    @Autowired
    GroupRepository groupRepository;

    Group group1;
    Group group2;

    @BeforeEach
    void init() {
        group1 = Group.createGroupBuilder("testGroup1");
        group2 = Group.createGroupBuilder("testGroup2");
        groupRepository.save(group1);
        groupRepository.save(group2);

        Keyword keyword1 = Keyword.createKeywordBuilder()
                .content("abc").group(group1).build();
        Keyword keyword2 = Keyword.createKeywordBuilder()
                .content("abc").group(group2).build();
        Keyword keyword3 = Keyword.createKeywordBuilder()
                .content("abc2").group(group2).build();

        keywordRepository.save(keyword1);
        keywordRepository.save(keyword2);
        keywordRepository.save(keyword3);

        for(int i = 0; i < 10; i++) {
            News news = News.createNewsBuilder()
                    .keyword("abc").title("test abc").link("http://")
                    .pubDate("pubDate").index("1000").build();
            newsRepository.save(news);
        }
        for(int i = 0; i < 10; i++) {
            News news = News.createNewsBuilder()
                    .keyword("abc2").title("test abc2").link("http://")
                    .pubDate("pubDate").index("1000").build();
            newsRepository.save(news);
        }
    }
    
    @Test
    @DisplayName("그룹 뉴스 확인")
    void searchGroupNewsTest() {
        //given
        SearchNewsDto searchCondition = new SearchNewsDto("1000");
        Pageable pageable = PageRequest.of(0, 8);
        
        //when
        Page<NewsDto> news = newsRepository
                .searchGroupNews(group2.getId(), searchCondition, pageable);

        //then
        assertThat(news.getTotalElements()).isEqualTo(20);
        assertThat(news.getSize()).isEqualTo(8);
    }

    @Test
    @DisplayName("그룹 뉴스 키워드")
    void searchGroupAllKeyword() {
        //when
        List<String> keywords = newsRepository.searchGroupAllKeyword(group2.getId());

        //then
        assertThat(keywords.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("그룹 뉴스 키워드 여부 확인")
    void searchGroupKeyword() {
        //when
        Optional<Keyword> keyword1 = newsRepository.searchGroupKeyword(group2.getId(), "abc");
        Optional<Keyword> keyword2 = newsRepository.searchGroupKeyword(group2.getId(), "abc12");

        //then
        Assertions.assertThat(keyword1.orElse(null)).isNotNull();
        Assertions.assertThatThrownBy(keyword2::get).isInstanceOf(NoSuchElementException.class);
    }
}