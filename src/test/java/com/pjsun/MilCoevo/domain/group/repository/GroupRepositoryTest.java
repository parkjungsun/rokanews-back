package com.pjsun.MilCoevo.domain.group.repository;

import com.pjsun.MilCoevo.domain.group.Group;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;
import java.util.Optional;

@DataJpaTest
class GroupRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    Group group;

    @BeforeEach
    void init() {

        group = Group.createGroupBuilder("testGroup");
        groupRepository.save(group);
    }

    @Test
    @DisplayName("그룹 조회 By 초대코드")
    void findGroupByInviteCodeTest() {
        //when
        Optional<Group> success = groupRepository
                .findGroupByInviteCode(group.getInviteCode());

        Optional<Group> fail = groupRepository
                .findGroupByInviteCode("random");

        //then
        Assertions.assertThat(success.orElse(null)).isNotNull();
        Assertions.assertThatThrownBy(fail::get).isInstanceOf(NoSuchElementException.class);
    }
}