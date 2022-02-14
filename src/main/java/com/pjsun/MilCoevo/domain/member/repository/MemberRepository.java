package com.pjsun.MilCoevo.domain.member.repository;

import com.pjsun.MilCoevo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

}
