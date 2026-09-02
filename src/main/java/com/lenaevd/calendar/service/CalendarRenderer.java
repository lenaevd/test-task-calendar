package com.lenaevd.calendar.service;

import com.lenaevd.calendar.util.RussianCalendarNames;
import com.lenaevd.calendar.entity.MonthEntity;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CalendarRenderer {

    private static final String WEEK_DAYS_STRING = "ПН ВТ СР ЧТ ПТ СБ ВС";
    private static final int CALENDAR_LENGTH = WEEK_DAYS_STRING.length();
    private static final int DAYS_IN_WEEK = 7;
    private static final int CELL_LENGTH = 2;
    private static final String LINE_SEPARATOR = "\n";

    /**
     * Создаёт красивую строчку для календаря
     * @param year год
     * @param months список месяцев
     * @return строку
     */
    public String renderYear(int year, List<MonthEntity> months) {
        StringBuilder sb = new StringBuilder();
        for (MonthEntity month : months) {
            sb.append(renderMonth(year, month));
            sb.append(LINE_SEPARATOR);
        }
        return sb.toString().stripTrailing();
    }

    /**
     * Создаёт красивую строчку для календаря конкретного месяца
     * @param year год
     * @param month месяц
     * @return строку
     */
    public String renderMonth(int year, MonthEntity month) {
        StringBuilder sb = new StringBuilder();
        appendHeader(sb, month.getNumber(), year);
        sb.append(LINE_SEPARATOR);
        sb.append(WEEK_DAYS_STRING).append(LINE_SEPARATOR);
        appendGrid(sb, month);
        return sb.toString().stripTrailing();
    }

    private void appendHeader(StringBuilder sb, int monthNumber, int year) {
        String monthName = RussianCalendarNames.monthOf(monthNumber);
        String yearStr = String.valueOf(year);
        int spaceNumber = CALENDAR_LENGTH - yearStr.length() - monthName.length();

        sb.append(monthName);
        sb.append(" ".repeat(spaceNumber));
        sb.append(yearStr);
    }

    private void appendGrid(StringBuilder sb, MonthEntity month) {
        List<List<Integer>> weeks = buildWeeks(month.getDaysCount(), month.getFirstDayOfWeek());
        for (List<Integer> week : weeks) {
            for (int column = 0; column < week.size(); column++) {
                appendCellContent(sb, week.get(column));
                sb.append(column == week.size() - 1 ? LINE_SEPARATOR : ' ');
            }
        }
    }

    private void appendCellContent(StringBuilder sb, Integer day) {
        if (day == null) {
            sb.append(" ".repeat(CELL_LENGTH));
        } else {
            if (day < 10) {
                sb.append(' ');
            }
            sb.append(day);
        }
    }

    private List<List<Integer>> buildWeeks(int daysInMonth, DayOfWeek firstDayOfWeek) {
        List<List<Integer>> weeks = new ArrayList<>();
        List<Integer> currentWeek = new ArrayList<>(Collections.nCopies(firstDayOfWeek.getValue() - 1, null));

        for (int day = 1; day <= daysInMonth; day++) {
            currentWeek.add(day);
            if (currentWeek.size() == DAYS_IN_WEEK) {
                weeks.add(currentWeek);
                currentWeek = new ArrayList<>();
            }
        }

        if (!currentWeek.isEmpty()) {
            while (currentWeek.size() < DAYS_IN_WEEK) {
                currentWeek.add(null);
            }
            weeks.add(currentWeek);
        }

        return weeks;
    }
}
