package com.pjsun.MilCoevo.domain.news.repository;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.news.Keyword;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class KeywordRepositoryTest {

    @Autowired
    KeywordRepository keywordRepository;

    @Autowired
    GroupRepository groupRepository;

    Group group1;
    Group group2;

    @BeforeEach
    void init() {
        group1 = Group.createGroupBuilder("testGroup1");
        group2 = Group.createGroupBuilder("testGroup2");
        groupRepository.save(group1);
        groupRepository.save(group2);

        Keyword keyword1 = Keyword.createKeywordBuilder()
                .content("abc").group(group1).build();
        Keyword keyword2 = Keyword.createKeywordBuilder()
                .content("abc").group(group2).build();
        Keyword keyword3 = Keyword.createKeywordBuilder()
                .content("abc2").group(group2).build();

        keywordRepository.save(keyword1);
        keywordRepository.save(keyword2);
        keywordRepository.save(keyword3);
    }

    @Test
    @DisplayName("Distinct 키워드 전체조회")
    void findAllKeywordTest() {
        //when
        List<String> keywords = keywordRepository.findAllKeyword();

        //then
        assertThat(keywords.size()).isEqualTo(2);
    }

}