package com.pjsun.MilCoevo.domain.news.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordsDto {

    private Long id;
    private String content;

    @QueryProjection
    public KeywordsDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
