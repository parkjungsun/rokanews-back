package com.pjsun.MilCoevo.domain.news.repository;

import com.pjsun.MilCoevo.domain.news.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {
}
