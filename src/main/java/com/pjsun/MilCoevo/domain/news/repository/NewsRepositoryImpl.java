package com.pjsun.MilCoevo.domain.news.repository;

import com.pjsun.MilCoevo.domain.group.QGroup;
import com.pjsun.MilCoevo.domain.news.Keyword;
import com.pjsun.MilCoevo.domain.news.QKeyword;
import com.pjsun.MilCoevo.domain.news.QNews;
import com.pjsun.MilCoevo.domain.news.dto.NewsDto;
import com.pjsun.MilCoevo.domain.news.dto.QNewsDto;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static com.pjsun.MilCoevo.domain.group.QGroup.group;
import static com.pjsun.MilCoevo.domain.news.QKeyword.keyword;
import static com.pjsun.MilCoevo.domain.news.QNews.news;
import java.util.List;
import java.util.Optional;

public class NewsRepositoryImpl implements NewsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NewsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<NewsDto> searchGroupNews(Long groupId, Pageable pageable) {
        List<NewsDto> newsDtos = queryFactory
                .select(
                        new QNewsDto(
                                news.id,
                                news.keyword,
                                news.title,
                                news.imageLink,
                                news.link,
                                news.pubDate,
                                news.index
                        )
                ).from(news)
                .where(news.keyword.in(
                        JPAExpressions
                                .select(keyword.content)
                                .from(keyword)
                                .where(keyword.group.id.eq(groupId))
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(news.count())
                .from(news)
                .where(news.keyword.in(
                        JPAExpressions
                                .select(keyword.content)
                                .from(keyword)
                                .where(keyword.group.id.eq(groupId))
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchOne();
        if(total == null) total = 0L;
        return new PageImpl<>(newsDtos, pageable, total);
    }

    @Override
    public Optional<Keyword> searchGroupKeyword(Long groupId, String key) {

        Keyword result = queryFactory
                .selectFrom(QKeyword.keyword)
                .where(
                        QKeyword.keyword.group.id.eq(groupId),
                        QKeyword.keyword.content.eq(key)
                ).fetchOne();

        return Optional.ofNullable(result);
    }
}