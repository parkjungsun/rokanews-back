package com.pjsun.MilCoevo.domain.notice.repository;

import com.pjsun.MilCoevo.domain.notice.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
