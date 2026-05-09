package com.sovai.platform.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateUtils {

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("UTC");

    public static Instant now() {
        return Instant.now();
    }

    public static LocalDate localDateFrom(Instant instant) {
        return instant.atZone(DEFAULT_ZONE).toLocalDate();
    }

    public static Instant instantFromLocalDate(LocalDate date) {
        return date.atStartOfDay(DEFAULT_ZONE).toInstant();
    }

    public static boolean isInPast(Instant instant) {
        return instant.isBefore(Instant.now());
    }

    public static boolean isInFuture(Instant instant) {
        return instant.isAfter(Instant.now());
    }
}

