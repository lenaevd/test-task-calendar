package com.lenaevd.calendar.dto;

import java.util.List;

public record MonthDto(int year, int number, String name, int daysCount, String firstDayOfWeek,
                       List<DayDto> days) {
}
