package com.pjsun.MilCoevo.domain.purchase.repository;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.absence.dto.SearchAbsenceDto;
import com.pjsun.MilCoevo.domain.purchase.Purpose;
import com.pjsun.MilCoevo.domain.purchase.QPurchase;
import com.pjsun.MilCoevo.domain.purchase.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.pjsun.MilCoevo.domain.absence.QAbsence.absence;
import static com.pjsun.MilCoevo.domain.purchase.QPurchase.purchase;
import static com.pjsun.MilCoevo.domain.schedule.QSchedule.schedule;

public class PurchaseRepositoryImpl implements PurchaseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PurchaseRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PurchaseResponseDto> searchPurchases(
            Long groupId, SearchPurchaseDto searchCondition, Pageable pageable) {
        List<PurchaseResponseDto> result = queryFactory
                .select(new QPurchaseResponseDto(
                        purchase.id,
                        purchase.title,
                        purchase.purpose,
                        purchase.purchasePrice,
                        purchase.drafter.position.as("drafterPosition"),
                        purchase.drafter.nickname.as("drafterNickname"),
                        purchase.purchaseDate,
                        purchase.processStatus
                ))
                .from(purchase)
                .where(
                        purchase.group.id.eq(groupId),
                        frontDate(searchCondition.getFrontDate()),
                        rearDate(searchCondition.getRearDate()),
                        processStatus(searchCondition.getProcessStatus())
                )
                .orderBy(purchase.purchaseDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(purchase.count())
                .from(purchase)
                .where(
                        purchase.group.id.eq(groupId),
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

    @Override
    public PurchasesResponseDto searchPurchaseStatistics(
            Long groupId, SearchPurchaseDto searchCondition) {

        return queryFactory
                .select(new QPurchasesResponseDto(
                            purchase.purchasePrice.sum().as("total"),
                            new CaseBuilder()
                                    .when(purchase.purpose.eq(Purpose.OFFICE))
                                    .then(purchase.purchasePrice)
                                    .otherwise(0L).sum().as("office"),
                            new CaseBuilder()
                                    .when(purchase.purpose.eq(Purpose.LECTURE))
                                    .then(purchase.purchasePrice)
                                    .otherwise(0L).sum().as("lecture"),
                            new CaseBuilder()
                                    .when(purchase.purpose.eq(Purpose.TRAVEL))
                                    .then(purchase.purchasePrice)
                                    .otherwise(0L).sum().as("travel"),
                            new CaseBuilder()
                                    .when(purchase.purpose.eq(Purpose.ETC_PURPOSE))
                                    .then(purchase.purchasePrice)
                                    .otherwise(0L).sum().as("etc")
                ))
                .from(purchase)
                .where(
                        purchase.group.id.eq(groupId),
                        frontDate(searchCondition.getFrontDate()),
                        rearDate(searchCondition.getRearDate()),
                        processStatus(searchCondition.getProcessStatus())
                )
                .fetchOne();
    }

    private BooleanExpression frontDate(LocalDate frontDate) {
        return frontDate != null ? purchase.purchaseDate.goe(frontDate) : null;
    }

    private BooleanExpression rearDate(LocalDate rearDate) {
        return rearDate != null ? purchase.purchaseDate.loe(rearDate) : null;
    }

    private BooleanExpression processStatus(ProcessStatus processStatus) {
        return processStatus != null ? purchase.processStatus.eq(processStatus) : null;
    }
}
