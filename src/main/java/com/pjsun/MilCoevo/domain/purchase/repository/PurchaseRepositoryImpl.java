package com.pjsun.MilCoevo.domain.purchase.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class PurchaseRepositoryImpl implements PurchaseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PurchaseRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
