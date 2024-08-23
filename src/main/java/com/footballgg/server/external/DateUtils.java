package com.footballgg.server.external;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtils {
    public static LocalDate getStartOfWeek(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    public static LocalDate getEndOfWeek(LocalDate date) {
        return date.with(DayOfWeek.SUNDAY);
    }
}
