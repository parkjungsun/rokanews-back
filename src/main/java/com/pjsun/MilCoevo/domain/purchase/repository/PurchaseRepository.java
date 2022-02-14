package com.pjsun.MilCoevo.domain.purchase.repository;

import com.pjsun.MilCoevo.domain.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>, PurchaseRepositoryCustom {
}
