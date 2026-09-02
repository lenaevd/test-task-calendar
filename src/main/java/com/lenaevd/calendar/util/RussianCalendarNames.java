package com.lenaevd.calendar.util;

import java.time.DayOfWeek;

public final class RussianCalendarNames {

    private static final String[] NAMES = {
            "ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ",
            "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"
    };

    private RussianCalendarNames() {
    }

    public static String monthOf(int monthNumber) {
        if (monthNumber < 1 || monthNumber > 12) {
            throw new IllegalArgumentException("Номер месяца должен быть от 1 до 12");
        }
        return NAMES[monthNumber - 1];
    }

    public static String dayOf(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "ПОНЕДЕЛЬНИК";
            case TUESDAY -> "ВТОРНИК";
            case WEDNESDAY -> "СРЕДА";
            case THURSDAY -> "ЧЕТВЕРГ";
            case FRIDAY -> "ПЯТНИЦА";
            case SATURDAY -> "СУББОТА";
            case SUNDAY -> "ВОСКРЕСЕНЬЕ";
        };
    }
}
