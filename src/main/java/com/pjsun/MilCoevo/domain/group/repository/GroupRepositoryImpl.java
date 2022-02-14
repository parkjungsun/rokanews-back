package com.pjsun.MilCoevo.domain.group.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class GroupRepositoryImpl implements GroupRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public GroupRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
