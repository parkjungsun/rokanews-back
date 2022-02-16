package com.pjsun.MilCoevo.domain.news.repository;

import com.pjsun.MilCoevo.domain.news.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    @Query("SELECT distinct k.content FROM Keyword k")
    List<String> findAllKeyword();
}
