package com.pjsun.MilCoevo.domain.notice.service;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.member.service.MemberServiceImpl;
import com.pjsun.MilCoevo.domain.notice.Comment;
import com.pjsun.MilCoevo.domain.notice.Notice;
import com.pjsun.MilCoevo.domain.notice.dto.NoticeResponseDto;
import com.pjsun.MilCoevo.domain.notice.dto.SearchNoticeDto;
import com.pjsun.MilCoevo.domain.notice.repository.CommentRepository;
import com.pjsun.MilCoevo.domain.notice.repository.NoticeRepository;
import com.pjsun.MilCoevo.exception.NoCommentException;
import com.pjsun.MilCoevo.exception.NoNoticeException;
import com.pjsun.MilCoevo.exception.NoPermissionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final MemberService memberService;
    private final GroupRepository groupRepository;
    private final NoticeRepository noticeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long addNotice(Long groupId, String title, String content) {

        Member member = memberService.getMemberByUserAndGroup(groupId);
        Group group = groupRepository.findById(groupId).orElseThrow(NoNoticeException::new);

        Notice notice = Notice.createNoticeBuilder()
                .title(title).content(content)
                .writer(member.sign()).group(group)
                .build();

        noticeRepository.save(notice);

        return notice.getId();
    }

    public NoticeResponseDto getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(NoNoticeException::new);

        return new NoticeResponseDto(notice);
    }

    public Page<NoticeResponseDto> getNotices(
            Long groupId, SearchNoticeDto searchCondition, Pageable pageable) {

        return noticeRepository.searchNotices(groupId, searchCondition, pageable);
    }

    @Transactional
    public void updateNotice(Long groupId, Long noticeId,
                             String title, String content) {
        Member member = memberService.getMemberByUserAndGroup(groupId);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(NoNoticeException::new);

        if (havePermission(member, notice)) {
            notice.updateTitleAndContent(title,content);
        } else {
            throw new NoPermissionException();
        }
    }

    @Transactional
    public void removeNotice(Long groupId, Long noticeId) {
        Member member = memberService.getMemberByUserAndGroup(groupId);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(NoNoticeException::new);

        if (havePermission(member, notice)) {
            notice.remove();
        } else {
            throw new NoPermissionException();
        }
    }

    @Transactional
    public Long addComment(Long groupId, Long noticeId, String content) {
        Member member = memberService.getMemberByUserAndGroup(groupId);
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(NoNoticeException::new);

        Comment comment = Comment.createCommentBuilder()
                        .content(content).writer(member.sign())
                        .notice(notice).build();

        commentRepository.save(comment);

        return comment.getId();
    }

    @Transactional
    public void removeComment(Long groupId, Long commentId) {
        Member member = memberService.getMemberByUserAndGroup(groupId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NoCommentException::new);

        if (havePermission(member, comment)) {
            comment.remove();
        } else {
            throw new NoPermissionException();
        }
    }

    private boolean havePermission(Member member, Notice notice) {
        if(member.getRank().equals(Rank.LEADER)) {
            return true;
        }
        return notice.getWriter().getEmail().equals(member.getInfo().getEmail());
    }

    private boolean havePermission(Member member, Comment comment) {
        if(member.getRank().equals(Rank.LEADER)) {
            return true;
        }
        return comment.getWriter().getEmail().equals(member.getInfo().getEmail());
    }
}
