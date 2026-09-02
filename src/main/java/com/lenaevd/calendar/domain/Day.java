package com.lenaevd.calendar.domain;

import java.time.DayOfWeek;
import java.time.MonthDay;

public record Day(MonthDay date, DayOfWeek dayOfWeek) {
}
