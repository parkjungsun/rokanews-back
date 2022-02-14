package com.pjsun.MilCoevo.domain.group.repository;

import com.pjsun.MilCoevo.domain.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {

    Optional<Group> findGroupByInviteCode(String inviteCode);

}
