package com.pjsun.MilCoevo.domain.notice.repository;

import com.pjsun.MilCoevo.domain.notice.dto.NoticeResponseDto;
import com.pjsun.MilCoevo.domain.notice.dto.SearchNoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<NoticeResponseDto> searchNotices(Long groupId, SearchNoticeDto searchCondition, Pageable pageable);

}
