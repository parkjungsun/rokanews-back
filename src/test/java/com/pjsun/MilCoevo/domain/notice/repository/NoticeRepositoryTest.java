package com.pjsun.MilCoevo.domain.notice.repository;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.domain.notice.Notice;
import com.pjsun.MilCoevo.domain.notice.dto.NoticeResponseDto;
import com.pjsun.MilCoevo.domain.notice.dto.SearchNoticeDto;
import com.pjsun.MilCoevo.domain.purchase.Item;
import com.pjsun.MilCoevo.domain.purchase.Purchase;
import com.pjsun.MilCoevo.domain.purchase.Purpose;
import com.pjsun.MilCoevo.domain.purchase.repository.ItemRepository;
import com.pjsun.MilCoevo.domain.purchase.repository.PurchaseRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NoticeRepositoryTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    GroupRepository groupRepository;

    Group group;

    @BeforeEach
    void init()  {
        group = Group.createGroupBuilder("testGroup");
        groupRepository.save(group);

        Identification sign = Identification.createIdentificationBuilder()
                .email("schedule@test.com").nickname("schedule")
                .position("scheduler").build();

        Notice notice = Notice.createNoticeBuilder()
                .title("testNotice").content("content")
                .writer(sign).group(group).build();

        noticeRepository.save(notice);
    }

    @Test
    @DisplayName("공지 검색 like 제목")
    void searchNoticesTest() {
        //given
        SearchNoticeDto searchCondition = new SearchNoticeDto("test");
        Pageable pageable = PageRequest.of(0,10);

        //when
        Page<NoticeResponseDto> response = noticeRepository
                .searchNotices(group.getId(), searchCondition, pageable);

        //then
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getContent().size()).isEqualTo(1);
    }
}