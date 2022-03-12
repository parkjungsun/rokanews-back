package com.pjsun.MilCoevo.domain.member.repository;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.group.QGroup;
import com.pjsun.MilCoevo.domain.group.dto.SearchGroupMemberDto;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.QMember;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.dto.QMemberGroupDto;
import com.pjsun.MilCoevo.domain.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.pjsun.MilCoevo.domain.absence.QAbsence.absence;
import static com.pjsun.MilCoevo.domain.group.QGroup.group;
import static com.pjsun.MilCoevo.domain.member.QMember.member;
import static com.pjsun.MilCoevo.domain.user.QUser.user;
import static com.querydsl.core.types.ExpressionUtils.count;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Member> searchMemberByUserIdAndGroupId(Long userId, Long groupId) {
        Member result = queryFactory
                .selectFrom(member)
                .where(
                        member.user.id.eq(userId),
                        member.group.id.eq(groupId))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Page<MemberGroupDto> searchMembersByUserId(Long userId, Pageable pageable) {
        List<MemberGroupDto> result = queryFactory
                .select(new QMemberGroupDto(
                        member.group.id.as("groupId"),
                        member.group.groupName,
                        member.rank,
                        member.info.email,
                        member.info.position,
                        member.info.nickname,
                        member.lastVisitedDate
                )).from(member)
                .where(
                        member.user.id.eq(userId),
                        member.isAvailable.eq(true)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(member.count())
                .from(member)
                .where(
                        member.user.id.eq(userId),
                        member.isAvailable.eq(true)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchOne();
        if(total == null) total = 0L;
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<MemberGroupDto> searchMembersByGroupId(Long groupId, SearchGroupMemberDto searchCondition, Pageable pageable) {
        List<MemberGroupDto> result = queryFactory
                .select(new QMemberGroupDto(
                        member.group.id.as("groupId"),
                        member.group.groupName,
                        member.rank,
                        member.info.email,
                        member.info.position,
                        member.info.nickname,
                        member.lastVisitedDate
                )).from(member)
                .where(
                        member.group.id.eq(groupId),
                        searchName(searchCondition.getSearchName())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(member.count())
                .from(member)
                .where(
                        member.group.id.eq(groupId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchOne();
        if(total == null) total = 0L;
        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression searchName(String searchName) {
        return StringUtils.hasText(searchName) ? member.info.nickname.contains(searchName) : null;
    }
}
