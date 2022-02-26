package com.pjsun.MilCoevo.domain.notice.service;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Identification;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.PresentStatus;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.notice.Comment;
import com.pjsun.MilCoevo.domain.notice.Notice;
import com.pjsun.MilCoevo.domain.notice.dto.NoticeResponseDto;
import com.pjsun.MilCoevo.domain.notice.dto.SearchNoticeDto;
import com.pjsun.MilCoevo.domain.notice.repository.CommentRepository;
import com.pjsun.MilCoevo.domain.notice.repository.NoticeRepository;
import com.pjsun.MilCoevo.exception.NoCommentException;
import com.pjsun.MilCoevo.exception.NoNoticeException;
import com.pjsun.MilCoevo.exception.NoPermissionException;
import com.pjsun.MilCoevo.test.MockTest;
import com.pjsun.MilCoevo.test.builder.CommentBuilder;
import com.pjsun.MilCoevo.test.builder.GroupBuilder;
import com.pjsun.MilCoevo.test.builder.MemberBuilder;
import com.pjsun.MilCoevo.test.builder.NoticeBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class NoticeServiceImplTest extends MockTest {

    @InjectMocks
    NoticeServiceImpl noticeService;

    @Mock
    MemberService memberService;

    @Mock
    GroupRepository groupRepository;

    @Mock
    NoticeRepository noticeRepository;

    @Mock
    CommentRepository commentRepository;

    @Test
    @DisplayName("그룹 공지 추가")
    void addNoticeTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Group group = GroupBuilder.build(1L);
        Notice notice = NoticeBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(groupRepository.findById(any())).willReturn(Optional.of(group));
        given(noticeRepository.save(any())).willReturn(notice);

        //when
        noticeService.addNotice(group.getId(), notice.getTitle(), notice.getContent());
    }

    @Test
    @DisplayName("공지 조회")
    void getNoticeTest() {
        //given
        Notice notice = NoticeBuilder.build(1L);

        given(noticeRepository.findById(any())).willReturn(Optional.of(notice));

        //when
        NoticeResponseDto result = noticeService.getNotice(notice.getId());

        //then
        assertThat(result.getId()).isEqualTo(notice.getId());
        assertThat(result.getTitle()).isEqualTo(notice.getTitle());
        assertThat(result.getContent()).isEqualTo(notice.getContent());
        assertThat(result.getWriterEmail()).isEqualTo(notice.getWriter().getEmail());
        assertThat(result.getWriterPosition()).isEqualTo(notice.getWriter().getPosition());
        assertThat(result.getWriterNickname()).isEqualTo(notice.getWriter().getNickname());
    }

    @Test
    @DisplayName("공지 조회 실패")
    void getNoticeFailTest() {
        //given
        Notice notice = NoticeBuilder.build(1L);

        given(noticeRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> noticeService.getNotice(notice.getId()))
                .isInstanceOf(NoNoticeException.class);
    }

    @Test
    @DisplayName("그룹 공지 조회")
    void getPurchasesTest() {
        //given
        Page<NoticeResponseDto> notices = new PageImpl<>(
                new ArrayList<>(10),
                PageRequest.of(0,10),
                10L);

        given(noticeRepository.searchNotices(any(),any(),any())).willReturn(notices);

        //when
        Page<NoticeResponseDto> result = noticeService.getNotices(1L,
                new SearchNoticeDto("searchTitle"),
                PageRequest.of(0, 10));

        //then
        assertThat(result.getTotalElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("공지 업데이트")
    void updateNoticeTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Notice notice = NoticeBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(noticeRepository.findById(any())).willReturn(Optional.of(notice));

        //then
        noticeService.updateNotice(1L,
                notice.getId(), notice.getTitle(), notice.getContent());
    }

    @Test
    @DisplayName("공지 업데이트 실패 No Notice")
    void updateNoticeFailTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Notice notice = NoticeBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(noticeRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> noticeService.updateNotice(1L,
                notice.getId(), notice.getTitle(), notice.getContent()))
                .isInstanceOf(NoNoticeException.class);
    }

    @Test
    @DisplayName("공지 업데이트 실패 No Permission")
    void updateNoticeFail2Test() {
        //given
        Member member = new Member(1L, Identification.createIdentificationBuilder()
                .email("test").nickname("ni").position("po").build(), Rank.MEMBER,
                PresentStatus.MISSED, true, LocalDateTime.now(),
                null, null);
        Notice notice = NoticeBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(noticeRepository.findById(any())).willReturn(Optional.of(notice));

        //then
        assertThatThrownBy(() -> noticeService.updateNotice(1L,
                notice.getId(), notice.getTitle(), notice.getContent()))
                .isInstanceOf(NoPermissionException.class);
    }

    @Test
    @DisplayName("공지 삭제 성공")
    void removeNoticeTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Notice notice = NoticeBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(noticeRepository.findById(any())).willReturn(Optional.of(notice));

        //then
        noticeService.removeNotice(1L, notice.getId());
    }

    @Test
    @DisplayName("공지 삭제 실패 No Notice")
    void removeNoticeFailTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Notice notice = NoticeBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(noticeRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> noticeService.removeNotice(1L, notice.getId()))
                .isInstanceOf(NoNoticeException.class);
    }

    @Test
    @DisplayName("공지 삭제 실패 No Permission")
    void removeNoticeFail2Test() {
        //given
        Member member = new Member(1L, Identification.createIdentificationBuilder()
                .email("test").nickname("ni").position("po").build(), Rank.MEMBER,
                PresentStatus.MISSED, true, LocalDateTime.now(),
                null, null);
        Notice notice = NoticeBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(noticeRepository.findById(any())).willReturn(Optional.of(notice));

        //then
        assertThatThrownBy(() -> noticeService.removeNotice(1L, notice.getId()))
                .isInstanceOf(NoPermissionException.class);
    }

    @Test
    @DisplayName("댓글 추가")
    void addCommentTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Notice notice = NoticeBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(noticeRepository.findById(any())).willReturn(Optional.of(notice));

        //then
        noticeService.addComment(1L, notice.getId(), "content");
    }

    @Test
    @DisplayName("댓글 추가 실패 No Notice")
    void addCommentFailTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Notice notice = NoticeBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(noticeRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> noticeService.addComment(1L, notice.getId(), "content"))
                .isInstanceOf(NoNoticeException.class);
    }

    @Test
    @DisplayName("댓글 삭제")
    void removeCommentTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Comment comment = CommentBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(commentRepository.findById(any())).willReturn(Optional.of(comment));

        //then
        noticeService.removeComment(1L, comment.getId());
    }

    @Test
    @DisplayName("댓글 삭제 실패 No Notice")
    void removeCommentFailTest() {
        //given
        Member member = MemberBuilder.build(1L);
        Comment comment = CommentBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(commentRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> noticeService.removeComment(1L, comment.getId()))
                .isInstanceOf(NoCommentException.class);
    }

    @Test
    @DisplayName("댓글 삭제 실패 No Permission")
    void removeCommentFail2Test() {
        //given
        Member member = new Member(1L, Identification.createIdentificationBuilder()
                .email("test").nickname("ni").position("po").build(), Rank.MEMBER,
                PresentStatus.MISSED, true, LocalDateTime.now(),
                null, null);
        Comment comment = CommentBuilder.build(1L);

        given(memberService.getMemberByUserAndGroup(any())).willReturn(member);
        given(commentRepository.findById(any())).willReturn(Optional.of(comment));

        //then
        assertThatThrownBy(() -> noticeService.removeComment(1L, comment.getId()))
                .isInstanceOf(NoPermissionException.class);
    }
}