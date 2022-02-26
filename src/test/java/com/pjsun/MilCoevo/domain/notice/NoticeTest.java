package com.pjsun.MilCoevo.domain.notice;

import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.test.DomainTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeTest extends DomainTest {

    @Test
    @DisplayName("공지 생성")
    void createNoticeTest() {
        //given
        String title = "title";
        String content = "content";
        Identification info = Identification.createIdentificationBuilder()
                .email("test@test.com").position("position")
                .nickname("nickname").build();

        //when
        Notice notice = Notice.createNoticeBuilder()
                .title(title).content(content)
                .writer(info).build();

        //then
        assertThat(notice.getTitle()).isEqualTo(title);
        assertThat(notice.getContent()).isEqualTo(content);
        assertThat(notice.getWriter()).isEqualTo(info);
        assertThat(notice.isAvailable()).isEqualTo(true);
    }

    @Test
    @DisplayName("비즈니스 로직")
    void businessTest() {
        //given
        String title = "title";
        String content = "content";
        Identification info = Identification.createIdentificationBuilder()
                .email("test@test.com").position("position")
                .nickname("nickname").build();

        Notice notice = Notice.createNoticeBuilder()
                .title(title).content(content)
                .writer(info).build();

        String updateTitle = "updateTitle";
        String updateContent = "updateContent";

        //when
        notice.updateTitleAndContent(updateTitle, updateContent);
        notice.remove();

        //then
        assertThat(notice.getTitle()).isEqualTo(updateTitle);
        assertThat(notice.getContent()).isEqualTo(updateContent);
        assertThat(notice.isAvailable()).isEqualTo(false);
    }
}