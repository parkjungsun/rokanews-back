package com.pjsun.MilCoevo.domain.notice;

import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.test.DomainTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest extends DomainTest {

    @Test
    @DisplayName("댓글 생성")
    void createCommentTest() {
        //given
        String content = "bla bla";
        Identification info = Identification.createIdentificationBuilder()
                .email("test@test.com").position("position")
                .nickname("nickname").build();

        //when
        Comment comment = Comment.createCommentBuilder()
                .content(content).writer(info).build();

        //then
        Assertions.assertThat(comment.getContent()).isEqualTo(content);
        Assertions.assertThat(comment.getWriter()).isEqualTo(info);
        Assertions.assertThat(comment.isAvailable()).isEqualTo(true);

        comment.remove();
        Assertions.assertThat(comment.isAvailable()).isEqualTo(false);
    }
}