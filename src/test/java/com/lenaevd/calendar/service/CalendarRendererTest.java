package com.lenaevd.calendar.service;

import com.lenaevd.calendar.entity.MonthEntity;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalendarRendererTest {

    private final CalendarRenderer renderer = new CalendarRenderer();

    @Test
    void renderMonth_januaryStartingOnMonday_rendersRightGrid() {
        MonthEntity january = new MonthEntity(null, 1, 31, DayOfWeek.MONDAY);

        String result = renderer.renderMonth(2024, january);
        String expected =
                """
                        ЯНВАРЬ          2024
                        ПН ВТ СР ЧТ ПТ СБ ВС
                         1  2  3  4  5  6  7
                         8  9 10 11 12 13 14
                        15 16 17 18 19 20 21
                        22 23 24 25 26 27 28
                        29 30 31""";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void renderMonth_septemberStartingOnSunday_padsFirstWeekWithBlankCells() {
        MonthEntity september = new MonthEntity(null, 9, 30, DayOfWeek.SUNDAY);

        String result = renderer.renderMonth(2024, september);
        String expected = """
                СЕНТЯБРЬ        2024
                ПН ВТ СР ЧТ ПТ СБ ВС
                                   1
                 2  3  4  5  6  7  8
                 9 10 11 12 13 14 15
                16 17 18 19 20 21 22
                23 24 25 26 27 28 29
                30""";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void renderYear_containsAllTwelveMonthHeadersInOrder() {
        int year = 2026;
        List<MonthEntity> months = twelveUnrealMonths();
        String result = renderer.renderYear(year, months);
        String[] namesInOrder = {
                "ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ",
                "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"
        };
        int previousIndex = -1;
        for (String name : namesInOrder) {
            int currentIndex = result.indexOf(name);
            assertThat(currentIndex).isGreaterThan(previousIndex);
            previousIndex = currentIndex;
        }
    }

    private List<MonthEntity> twelveUnrealMonths() {
        List<MonthEntity> months = new ArrayList<>(12);
        for (int number = 1; number <= 12; number++) {
            months.add(new MonthEntity(null, number, 30, DayOfWeek.MONDAY));
        }
        return months;
    }
}
