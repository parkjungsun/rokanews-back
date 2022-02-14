package com.pjsun.MilCoevo.domain.absence.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class AbsenceRepositoryImpl implements AbsenceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AbsenceRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
