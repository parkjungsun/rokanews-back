package com.pjsun.MilCoevo.domain.purchase.dto;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchPurchaseDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate frontDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rearDate;

    private ProcessStatus processStatus;
}
