package com.pjsun.MilCoevo.domain.news.dto;

import com.pjsun.MilCoevo.domain.news.News;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NewsResponseDto {

    private String lastBuildDate;
    private String total;
    private String start;
    private String display;
    private List<NewsDto> items;

}
