package com.pjsun.MilCoevo.domain.news.service;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.news.Keyword;
import com.pjsun.MilCoevo.domain.news.dto.KeywordsDto;
import com.pjsun.MilCoevo.domain.news.dto.NewsDto;
import com.pjsun.MilCoevo.domain.news.dto.SearchNewsDto;
import com.pjsun.MilCoevo.domain.news.repository.KeywordRepository;
import com.pjsun.MilCoevo.domain.news.repository.NewsRepository;
import com.pjsun.MilCoevo.exception.NoExistGroupException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService{

    private final GroupRepository groupRepository;
    private final NewsRepository newsRepository;
    private final KeywordRepository keywordRepository;

    public Page<NewsDto> getGroupNews(Long groupId, SearchNewsDto searchCondition, Pageable pageable) {
        return newsRepository.searchGroupNews(groupId, searchCondition, pageable);
    }

    @Transactional
    public Long addKeyword(Long groupId, String keyword) {
        Keyword key = newsRepository.searchGroupKeyword(groupId, keyword).orElse(null);

        if(key == null) {
            Group group = groupRepository.findById(groupId).orElseThrow(NoExistGroupException::new);
            key = Keyword.createKeyword(keyword, group);

            keywordRepository.save(key);
        }

        return key.getId();
    }

    @Transactional
    public void removeKeyword(Long keywordId) {
        keywordRepository.deleteById(keywordId);
    }

    public List<KeywordsDto> getGroupKeywords(Long groupId) {
       return newsRepository.searchGroupAllKeyword(groupId);
    }
}
