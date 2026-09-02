package com.lenaevd.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.MonthDay;

public record DayDto(@JsonFormat(pattern = "MM-dd") MonthDay date, String dayOfWeek) {
}