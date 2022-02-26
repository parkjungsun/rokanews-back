package com.pjsun.MilCoevo.domain.news.service;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.news.Keyword;
import com.pjsun.MilCoevo.domain.news.dto.NewsDto;
import com.pjsun.MilCoevo.domain.news.dto.SearchNewsDto;
import com.pjsun.MilCoevo.domain.news.repository.KeywordRepository;
import com.pjsun.MilCoevo.domain.news.repository.NewsRepository;
import com.pjsun.MilCoevo.test.MockTest;
import com.pjsun.MilCoevo.test.builder.GroupBuilder;
import com.pjsun.MilCoevo.test.builder.KeywordBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class NewsServiceImplTest extends MockTest {

    @InjectMocks
    NewsServiceImpl newsService;

    @Mock
    GroupRepository groupRepository;

    @Mock
    NewsRepository newsRepository;

    @Mock
    KeywordRepository keywordRepository;

    @Test
    @DisplayName("그룹 뉴스 조회")
    void getGroupNewsTest() {
        //given
        Page<NewsDto> news = new PageImpl<>(
                new ArrayList<>(10),
                PageRequest.of(0,10),
                10L);

        given(newsRepository.searchGroupNews(any(),any(),any())).willReturn(news);

        //when
        Page<NewsDto> result = newsService.getGroupNews(1L,
                new SearchNewsDto("0130"),
                PageRequest.of(0, 10));

        //then
        assertThat(result.getTotalElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("그룹 키워드 추가")
    void addKeywordTest() {
        //given
        Keyword keyword = KeywordBuilder.build(1L);
        Group group = GroupBuilder.build(1L);

        given(newsRepository.searchGroupKeyword(any(), any())).willReturn(Optional.of(keyword));
        //given(groupRepository.findById(any())).willReturn(Optional.of(group));
        //given(keywordRepository.save(any())).willReturn(keyword);

        //then
        newsService.addKeyword(group.getId(), keyword.getContent());
    }

    @Test
    @DisplayName("그룹 키워드 조회")
    void getGroupKeywordsTest() {
        //given
        List<String> keywords = new ArrayList<>(10);

        given(newsRepository.searchGroupAllKeyword(any())).willReturn(keywords);
        //when
        List<String> result = newsService.getGroupKeywords(1L);
    }
}