package com.pjsun.MilCoevo.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    // LocalDate -> LocalDateTime
    public static LocalDateTime from(LocalDate localDate) {
        return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(),0,0);
    }

    // LocalDateTime -> LocalDate
    public static LocalDate from(LocalDateTime localDateTime) {
        return LocalDate.of(localDateTime.getYear(),localDateTime.getMonth(), localDateTime.getDayOfMonth());
    }
}
