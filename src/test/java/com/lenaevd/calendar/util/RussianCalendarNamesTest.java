package com.lenaevd.calendar.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RussianCalendarNamesTest {

    @ParameterizedTest(name = "месяц {0} -> {1}")
    @CsvSource({
            "1,  ЯНВАРЬ",
            "2,  ФЕВРАЛЬ",
            "3,  МАРТ",
            "4,  АПРЕЛЬ",
            "5,  МАЙ",
            "6,  ИЮНЬ",
            "7,  ИЮЛЬ",
            "8,  АВГУСТ",
            "9,  СЕНТЯБРЬ",
            "10, ОКТЯБРЬ",
            "11, НОЯБРЬ",
            "12, ДЕКАБРЬ"
    })
    void monthOf_validNumber_returnsRussianName(int number, String expectedName) {
        assertThat(RussianCalendarNames.monthOf(number)).isEqualTo(expectedName);
    }

    @ParameterizedTest(name = "номер месяца {0} невалиден")
    @ValueSource(ints = {0, -1, 13, 100})
    void monthOf_invalidNumber_throwsIllegalArgumentException(int invalidNumber) {
        assertThatThrownBy(() -> RussianCalendarNames.monthOf(invalidNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{0} -> непустое русское название")
    @EnumSource(DayOfWeek.class)
    void dayOf_everyEnumValue_returnsNonBlankName(DayOfWeek dayOfWeek) {
        assertThat(RussianCalendarNames.dayOf(dayOfWeek)).isNotBlank();
    }

    @Test
    void dayOf_monday_returnsExactRussianName() {
        assertThat(RussianCalendarNames.dayOf(DayOfWeek.MONDAY)).isEqualTo("ПОНЕДЕЛЬНИК");
    }
}
