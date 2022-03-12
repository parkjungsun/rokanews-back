package com.pjsun.MilCoevo.domain.news.service;

import com.pjsun.MilCoevo.domain.news.Keyword;
import com.pjsun.MilCoevo.domain.news.dto.KeywordsDto;
import com.pjsun.MilCoevo.domain.news.dto.NewsDto;
import com.pjsun.MilCoevo.domain.news.dto.SearchNewsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {

    Page<NewsDto> getGroupNews(Long groupId,
                               SearchNewsDto searchCondition,
                               Pageable pageable);

    List<KeywordsDto> getGroupKeywords(Long groupId);

    Long addKeyword(Long groupId, String keyword);

    void removeKeyword(Long keywordId);

}
