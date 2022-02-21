package com.pjsun.MilCoevo.domain.group.repository;

import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.dto.QMemberGroupDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.pjsun.MilCoevo.domain.member.QMember.member;

public class GroupRepositoryImpl implements GroupRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public GroupRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

}
