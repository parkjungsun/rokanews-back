package com.pjsun.MilCoevo.domain.notice.repository;

import com.pjsun.MilCoevo.domain.notice.QNotice;
import com.pjsun.MilCoevo.domain.notice.dto.NoticeResponseDto;
import com.pjsun.MilCoevo.domain.notice.dto.QNoticeResponseDto;
import com.pjsun.MilCoevo.domain.notice.dto.SearchNoticeDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import static com.pjsun.MilCoevo.domain.absence.QAbsence.absence;
import static com.pjsun.MilCoevo.domain.notice.QNotice.notice;

public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NoticeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<NoticeResponseDto> searchNotices(
            Long groupId, SearchNoticeDto searchCondition, Pageable pageable) {

        List<NoticeResponseDto> result = queryFactory
                .select(new QNoticeResponseDto(
                        notice.id,
                        notice.title,
                        notice.writer.position.as("writerPosition"),
                        notice.writer.nickname.as("writerNickname"),
                        notice.createdDate
                ))
                .from(notice)
                .where(
                        notice.group.id.eq(groupId),
                        searchTitle(searchCondition.getSearchTitle()),
                        notice.isAvailable.eq(true)
                )
                .orderBy(notice.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(notice.count())
                .from(notice)
                .where(
                        notice.group.id.eq(groupId),
                        searchTitle(searchCondition.getSearchTitle())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchOne();
        if(total == null) total = 0L;

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression searchTitle(String searchTitle) {
        return searchTitle != null ? notice.title.contains(searchTitle) : null;
    }
}
