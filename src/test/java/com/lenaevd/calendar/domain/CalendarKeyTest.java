package com.lenaevd.calendar.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.DayOfWeek;

import static org.assertj.core.api.Assertions.assertThat;

class CalendarKeyTest {

    @ParameterizedTest(name = "{0} год -> {1}, високосный: {2}")
    @CsvSource({
            "2024, MONDAY,   true",
            "2026, THURSDAY,   false",
            "1900, MONDAY,   false",
            "2000, SATURDAY, true"
    })
    void of_shouldComputeCorrectFirstDayAndLeapFlag(int year, DayOfWeek expectedFirstDay, boolean expectedLeap) {
        CalendarKey key = CalendarKey.of(year);

        assertThat(key.firstDayOfWeek()).isEqualTo(expectedFirstDay);
        assertThat(key.isLeap()).isEqualTo(expectedLeap);
    }

    @Test
    void of_calledTwiceForSameYear_shouldProduceEqualKeys() {
        assertThat(CalendarKey.of(2026)).isEqualTo(CalendarKey.of(2026));
    }

    @Test
    void of_differentYearsWithDifferentLayout_shouldNotBeEqual() {
        assertThat(CalendarKey.of(2025)).isNotEqualTo(CalendarKey.of(2026));
    }
}
