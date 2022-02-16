package com.pjsun.MilCoevo.domain.news.service;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.news.Keyword;
import com.pjsun.MilCoevo.domain.news.dto.NewsDto;
import com.pjsun.MilCoevo.domain.news.repository.KeywordRepository;
import com.pjsun.MilCoevo.domain.news.repository.NewsRepository;
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
public class NewsService {

    private final GroupRepository groupRepository;
    private final NewsRepository newsRepository;
    private final KeywordRepository keywordRepository;

    public Page<NewsDto> getGroupNews(Long groupId, Pageable pageable) {
        // TODO Check Author Member
        Page<NewsDto> news = newsRepository.searchGroupNews(groupId, pageable);

        return news;
    }

    @Transactional
    public Long addKeyword(Long groupId, String keyword) {
        // TODO Check Author Member

        Keyword key = newsRepository.searchGroupKeyword(groupId, keyword).orElse(null);

        if(key == null) {
            Group group = groupRepository.getById(groupId);
            key = Keyword.createKeyword(keyword, group);

            keywordRepository.save(key);
        }

        return key.getId();
    }

    @Transactional
    public void removeKeyword(Long keywordId) {
        // TODO Check Author Member
        keywordRepository.deleteById(keywordId);
    }
}
