package com.pjsun.MilCoevo.domain.purchase.repository;

import com.pjsun.MilCoevo.domain.purchase.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
