package com.pjsun.MilCoevo.domain.news.controller;

import com.pjsun.MilCoevo.domain.news.dto.NewKeywordRequestDto;
import com.pjsun.MilCoevo.domain.news.dto.NewsDto;
import com.pjsun.MilCoevo.domain.news.dto.SearchNewsDto;
import com.pjsun.MilCoevo.domain.news.service.NewsService;
import com.pjsun.MilCoevo.domain.news.service.NewsServiceImpl;
import com.pjsun.MilCoevo.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @Value("${error.common.bindingError}")
    private String BINDING_ERROR_EXCEPTION;

    @Value("${success.common.response}")
    private String SUCCESS_RESPONSE;

    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseDto> getGroupNews(
            @PathVariable Long groupId,
            @ModelAttribute SearchNewsDto searchCondition,
            Pageable pageable) {

        Page<NewsDto> news = newsService.getGroupNews(groupId, searchCondition, pageable);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, news);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{groupId}/keyword")
    public ResponseEntity<ResponseDto> getGroupKeywords (
            @PathVariable Long groupId) {

        List<String> groupKeywords = newsService.getGroupKeywords(groupId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, groupKeywords);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/keyword")
    public ResponseEntity<ResponseDto> addKeyword (
            @PathVariable Long groupId,
            @Validated @RequestBody NewKeywordRequestDto requestDto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ResponseDto error = new ResponseDto(BINDING_ERROR_EXCEPTION, bindingResult);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Long keywordId = newsService.addKeyword(groupId, requestDto.getKeyword());

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE, keywordId);

        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @DeleteMapping("/{groupId}/keyword/{keywordId}")
    public ResponseEntity<ResponseDto> removeKeyword (
            @PathVariable Long keywordId) {

        newsService.removeKeyword(keywordId);

        ResponseDto data = new ResponseDto(SUCCESS_RESPONSE);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
