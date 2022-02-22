package com.pjsun.MilCoevo.domain.schedule.repository;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.schedule.QSchedule;
import com.pjsun.MilCoevo.domain.schedule.dto.QScheduleResponseDto;
import com.pjsun.MilCoevo.domain.schedule.dto.ScheduleResponseDto;
import com.pjsun.MilCoevo.domain.schedule.dto.SearchScheduleDto;
import com.pjsun.MilCoevo.util.DateUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.pjsun.MilCoevo.domain.member.QMember.member;
import static com.pjsun.MilCoevo.domain.schedule.QSchedule.schedule;

public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ScheduleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ScheduleResponseDto> searchSchedules(
            Long groupId, SearchScheduleDto searchCondition, Pageable pageable) {

        List<ScheduleResponseDto> result = queryFactory
                .select(new QScheduleResponseDto(
                        schedule.id,
                        schedule.title,
                        schedule.workScope,
                        schedule.workDate,
                        schedule.drafter.position.as("drafterPosition"),
                        schedule.drafter.nickname.as("drafterNickname")
                ))
                .from(schedule)
                .where(
                        schedule.group.id.eq(groupId),
                        frontDate(searchCondition.getFrontDate()),
                        rearDate(searchCondition.getRearDate()),
                        processStatus(searchCondition.getProcessStatus())
                )
                .orderBy(schedule.workDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(schedule.count())
                .from(schedule)
                .where(
                        schedule.group.id.eq(groupId),
                        frontDate(searchCondition.getFrontDate()),
                        rearDate(searchCondition.getRearDate()),
                        processStatus(searchCondition.getProcessStatus())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchOne();
        if(total == null) total = 0L;

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression frontDate(LocalDate frontDate) {
        return frontDate != null ? schedule.workDate.goe(DateUtil.from(frontDate)) : null;
    }

    private BooleanExpression rearDate(LocalDate rearDate) {
        return rearDate != null ? schedule.workDate.loe(DateUtil.from(rearDate)) : null;
    }

    private BooleanExpression processStatus(ProcessStatus processStatus) {
        return processStatus != null ? schedule.processStatus.eq(processStatus) : null;
    }
}
