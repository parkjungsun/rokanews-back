package com.pjsun.MilCoevo.test.builder;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.purchase.Purchase;
import com.pjsun.MilCoevo.domain.purchase.Purpose;

import java.time.LocalDate;
import java.util.ArrayList;

public class PurchaseBuilder {

    public static Purchase build(Long id) {
        return new Purchase(id, "title", "content",
                Purpose.ETC_PURPOSE, ProcessStatus.SUGGESTED, 0L,
                LocalDate.now(), IdentificationBuilder.build(), null, null,
                null, new ArrayList<>());
    }
}
