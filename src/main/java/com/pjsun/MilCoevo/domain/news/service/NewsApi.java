package com.pjsun.MilCoevo.domain.news.service;

import com.pjsun.MilCoevo.domain.news.repository.KeywordRepository;
import com.pjsun.MilCoevo.domain.news.News;
import com.pjsun.MilCoevo.domain.news.dto.NewsResponseDto;
import com.pjsun.MilCoevo.domain.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsApi {

    @Value("${api.naver.clientId}")
    private String ClientId;

    @Value("${api.naver.clientSecret}")
    private String ClientSecret;

    @Value("${api.naver.newsUrl}")
    private String NewsApiURL;

    private final RestTemplate restTemplate;
    private final NewsRepository newsRepository;
    private final KeywordRepository keywordRepository;

    @Scheduled(cron = "0 0/30 * * * *")
    private void newsBot() {
        String index = getIndex();
        List<String> keywords = keywordRepository.findAllKeyword();
        for (String key : keywords) {
            log.debug("NewsBot start keyword = {}, index = {}", key, index);
            requestNews(key).getItems().forEach(newsDto -> {
                News news = newsDto.getNews(key, index);
                newsRepository.save(news);
                log.debug("NewsBot Compelete keyword = {}, index = {}", key, index);
            });
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void newsDelBot() {
        newsRepository.deleteAll();
    }

    private String getIndex() {
        int h = LocalDateTime.now().getHour();
        int m = LocalDateTime.now().getMinute();
        String fh = "";
        if(m >= 45) {
            fh = String.format("%02d", h + 1);
        } else {
            fh = String.format("%02d", h);
        }
        String fm = "";
        if(m >= 15 && m < 45) {
            fm = "30";
        } else {
            fm = "00";
        }

        return fh.concat(fm);
    }

    private NewsResponseDto requestNews(String keyword) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", ClientId);
        headers.add("X-Naver-Client-Secret", ClientSecret);

        final HttpEntity<String> entity= new HttpEntity<>(headers);

        return restTemplate.exchange(NewsApiURL, HttpMethod.GET, entity, NewsResponseDto.class, keyword, 100, 1).getBody();
    }
}
