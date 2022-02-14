package com.pjsun.MilCoevo.domain.notice.repository;

import com.pjsun.MilCoevo.domain.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {
}
