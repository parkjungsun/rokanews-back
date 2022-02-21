package com.pjsun.MilCoevo.domain.notice.service;

import com.pjsun.MilCoevo.domain.notice.dto.NoticeResponseDto;
import com.pjsun.MilCoevo.domain.notice.dto.SearchNoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {

    Page<NoticeResponseDto> getNotices(
            Long groupId, SearchNoticeDto searchCondition, Pageable pageable);

    NoticeResponseDto getNotice(Long noticeId);

    Long addNotice(Long groupId, String title, String content);

    void updateNotice(Long groupId, Long noticeId,
                             String title, String content);
    void removeNotice(Long groupId, Long noticeId);

    Long addComment(Long groupId, Long noticeId, String content);

    void removeComment(Long groupId, Long commentId);
}
