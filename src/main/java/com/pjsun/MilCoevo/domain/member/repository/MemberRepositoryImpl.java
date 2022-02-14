package com.pjsun.MilCoevo.domain.member.repository;

import com.pjsun.MilCoevo.domain.group.QGroup;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.QMember;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import com.pjsun.MilCoevo.domain.member.dto.QMemberGroupDto;
import com.pjsun.MilCoevo.domain.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.pjsun.MilCoevo.domain.group.QGroup.group;
import static com.pjsun.MilCoevo.domain.member.QMember.member;
import static com.pjsun.MilCoevo.domain.user.QUser.user;

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
    public List<MemberGroupDto> searchMembersByUserId(Long userId) {
        return queryFactory
                .select(new QMemberGroupDto(
                        member.group.id.as("groupId"),
                        member.group.groupName,
                        member.rank,
                        member.info.position,
                        member.info.nickname,
                        member.lastVisitedDate
                )).from(member)
                .where(
                        member.user.id.eq(userId),
                        member.isAvailable.eq(true)
                )
                .fetch();
    }

}
