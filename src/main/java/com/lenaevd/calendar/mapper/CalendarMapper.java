package com.lenaevd.calendar.mapper;

import com.lenaevd.calendar.dto.CalendarDto;
import com.lenaevd.calendar.dto.DayDto;
import com.lenaevd.calendar.dto.MonthDto;
import com.lenaevd.calendar.entity.CalendarTemplateEntity;
import com.lenaevd.calendar.domain.Day;
import com.lenaevd.calendar.util.RussianCalendarNames;
import com.lenaevd.calendar.entity.MonthEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = RussianCalendarNames.class)
public interface CalendarMapper {

    @Mapping(target = "year", source = "year")
    @Mapping(target = "isLeap", source = "template.leap")
    @Mapping(target = "firstDayOfWeek", expression = "java(RussianCalendarNames.dayOf(template.getFirstDayOfWeek()))")
    @Mapping(target = "months", expression = "java(toMonthDtoList(template.getMonths(), year))")
    CalendarDto toCalendarDto(CalendarTemplateEntity template, int year);

    @Mapping(target = "year", source = "year")
    @Mapping(target = "number", source = "month.number")
    @Mapping(target = "name", expression = "java(RussianCalendarNames.monthOf(month.getNumber()))")
    @Mapping(target = "daysCount", source = "month.daysCount")
    @Mapping(target = "firstDayOfWeek", expression = "java(RussianCalendarNames.dayOf(month.getFirstDayOfWeek()))")
    @Mapping(target = "days", expression = "java(toDayDtoList(month.getDaysList()))")
    MonthDto toMonthDto(MonthEntity month, int year);

    @Mapping(target = "date", source = "day.date")
    @Mapping(target = "dayOfWeek", expression = "java(RussianCalendarNames.dayOf(day.dayOfWeek()))")
    DayDto toDayDto(Day day);

    default List<MonthDto> toMonthDtoList(List<MonthEntity> months, int year) {
        return months.stream()
                .map(month -> toMonthDto(month, year))
                .toList();
    }

    default List<DayDto> toDayDtoList(List<Day> days) {
        return days.stream()
                .map(day -> toDayDto(day))
                .toList();
    }
}
