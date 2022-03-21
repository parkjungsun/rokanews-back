package com.pjsun.MilCoevo.domain.news.repository;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.group.QGroup;
import com.pjsun.MilCoevo.domain.news.Keyword;
import com.pjsun.MilCoevo.domain.news.QKeyword;
import com.pjsun.MilCoevo.domain.news.QNews;
import com.pjsun.MilCoevo.domain.news.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import static com.pjsun.MilCoevo.domain.group.QGroup.group;
import static com.pjsun.MilCoevo.domain.news.QKeyword.keyword;
import static com.pjsun.MilCoevo.domain.news.QNews.news;
import static com.pjsun.MilCoevo.domain.purchase.QPurchase.purchase;

import java.util.List;
import java.util.Optional;

public class NewsRepositoryImpl implements NewsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NewsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<NewsDto> searchGroupNews(Long groupId, SearchNewsDto searchCondition, Pageable pageable) {
        List<NewsDto> result = queryFactory
                .select(
                        new QNewsDto(
                                news.id.max(),
                                news.keyword.max(),
                                news.title.max(),
                                news.imageLink.max(),
                                news.link,
                                news.pubDate.max(),
                                news.timeIndex.max(),
                                news.published.max()
                        )
                ).from(news)
                .where(news.keyword.in(
                        JPAExpressions
                                .select(keyword.content)
                                .from(keyword)
                                .where(keyword.group.id.eq(groupId))),
                        timeIndex(searchCondition.getTimeIndex())
                )
                .groupBy(news.link)
                .orderBy(news.published.desc())
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
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public List<KeywordsDto> searchGroupAllKeyword(Long groupId) {
        return queryFactory
                .select(
                        new QKeywordsDto(
                                keyword.id,
                                keyword.content
                        )
                )
                .from(keyword)
                .where(keyword.group.id.eq(groupId))
                .fetch();
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

    private BooleanExpression timeIndex(String timeIndex) {
        return StringUtils.hasText(timeIndex) ? news.timeIndex.eq(timeIndex) : null;
    }
}