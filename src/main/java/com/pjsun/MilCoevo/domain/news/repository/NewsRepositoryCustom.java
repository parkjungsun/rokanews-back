package com.pjsun.MilCoevo.domain.news.repository;

import com.pjsun.MilCoevo.domain.news.Keyword;
import com.pjsun.MilCoevo.domain.news.dto.NewsDto;
import com.pjsun.MilCoevo.domain.news.dto.SearchNewsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface NewsRepositoryCustom {

    Page<NewsDto> searchGroupNews(Long groupId, SearchNewsDto searchCondition, Pageable pageable);
    List<String> searchGroupAllKeyword(Long groupId);
    Optional<Keyword> searchGroupKeyword(Long groupId, String keyword);
}
