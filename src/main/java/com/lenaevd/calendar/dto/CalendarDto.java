package com.lenaevd.calendar.dto;

import java.util.List;

public record CalendarDto(int year, boolean isLeap, String firstDayOfWeek, List<MonthDto> months) {
}
