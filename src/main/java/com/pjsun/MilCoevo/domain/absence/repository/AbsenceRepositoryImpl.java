package com.pjsun.MilCoevo.domain.absence.repository;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.absence.QAbsence;
import com.pjsun.MilCoevo.domain.absence.dto.AbsenceResponseDto;
import com.pjsun.MilCoevo.domain.absence.dto.QAbsenceResponseDto;
import com.pjsun.MilCoevo.domain.absence.dto.SearchAbsenceDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.pjsun.MilCoevo.domain.absence.QAbsence.absence;
import static com.pjsun.MilCoevo.domain.schedule.QSchedule.schedule;

public class AbsenceRepositoryImpl implements AbsenceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AbsenceRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<AbsenceResponseDto> searchAbsences(
            Long groupId, SearchAbsenceDto searchCondition, Pageable pageable) {

        List<AbsenceResponseDto> result = queryFactory
                .select(new QAbsenceResponseDto(
                        absence.id,
                        absence.title,
                        absence.startDate,
                        absence.endDate,
                        absence.reason,
                        absence.drafter.position.as("drafterPosition"),
                        absence.drafter.nickname.as("drafterNickname"),
                        absence.processStatus
                ))
                .from(absence)
                .where(
                        absence.group.id.eq(groupId),
                        frontDate(searchCondition.getFrontDate()),
                        rearDate(searchCondition.getRearDate()),
                        processStatus(searchCondition.getProcessStatus())
                )
                .orderBy(absence.startDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(absence.count())
                .from(absence)
                .where(
                        absence.group.id.eq(groupId),
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
        return frontDate != null ? absence.endDate.goe(frontDate) : null;
    }

    private BooleanExpression rearDate(LocalDate rearDate) {
        return rearDate != null ? absence.startDate.loe(rearDate) : null;
    }

    private BooleanExpression processStatus(ProcessStatus processStatus) {
        return processStatus != null ? absence.processStatus.eq(processStatus) : null;
    }
}
