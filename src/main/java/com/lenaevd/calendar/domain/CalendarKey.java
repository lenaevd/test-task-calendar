package com.lenaevd.calendar.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;

public record CalendarKey(DayOfWeek firstDayOfWeek, boolean isLeap) {
    public static CalendarKey of(int year) {
        DayOfWeek firstDayOfWeek = LocalDate.of(year, 1, 1).getDayOfWeek();
        return new CalendarKey(firstDayOfWeek, Year.isLeap(year));
    }
}
