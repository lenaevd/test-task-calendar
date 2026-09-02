package com.lenaevd.calendar.service;

import com.lenaevd.calendar.entity.CalendarTemplateEntity;
import com.lenaevd.calendar.entity.MonthEntity;
import com.lenaevd.calendar.repository.CalendarTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarServiceImplTest {

    @Mock
    private CalendarTemplateRepository repository;

    private CalendarServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CalendarServiceImpl(repository, new CalendarRenderer());
    }

    @Test
    void getOrCreateTemplate_whenTemplateAlreadyExists_returnsItWithoutSaving() {
        CalendarTemplateEntity existing = new CalendarTemplateEntity(DayOfWeek.MONDAY, true);
        when(repository.findByFirstDayOfWeekAndIsLeap(DayOfWeek.MONDAY, true))
                .thenReturn(Optional.of(existing));

        CalendarTemplateEntity result = service.getOrCreateTemplate(2024);

        assertThat(result).isSameAs(existing);
        verify(repository, never()).save(any());
    }

    @Test
    void getOrCreateTemplate_whenTemplateMissing_buildsAllTwelveMonthsAndSaves() {
        when(repository.findByFirstDayOfWeekAndIsLeap(any(), anyBoolean())).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CalendarTemplateEntity result = service.getOrCreateTemplate(2023); // 2023 (воскресенье, невисокосный)

        ArgumentCaptor<CalendarTemplateEntity> captor = ArgumentCaptor.forClass(CalendarTemplateEntity.class);
        verify(repository).save(captor.capture());

        CalendarTemplateEntity saved = captor.getValue();
        assertThat(saved.getFirstDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY);
        assertThat(saved.isLeap()).isFalse();
        assertThat(saved.getMonths()).hasSize(12);

        assertThat(saved.getMonths().stream().map(MonthEntity::getNumber).toList())
                .containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        MonthEntity january = saved.getMonths().get(0);
        assertThat(january.getFirstDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY);
        assertThat(january.getDaysCount()).isEqualTo(31);

        MonthEntity february = saved.getMonths().get(1);
        assertThat(february.getDaysCount()).isEqualTo(28);

        assertThat(result).isSameAs(saved);
    }

    @ParameterizedTest(name = "следующий год-аналог для {0} -> {1}")
    @MethodSource("nextYearWithSameTemplateCases")
    void findNextYearWithSameTemplate_returnsNearestMatchingYear(int year, int expectedNextYear) {
        assertThat(service.findNextYearWithSameTemplate(year)).isEqualTo(expectedNextYear);
    }

    static Stream<Arguments> nextYearWithSameTemplateCases() {
        return Stream.of(
                Arguments.of(2024, 2052),
                Arguments.of(2023, 2034)
        );
    }

    @ParameterizedTest(name = "{0} в диапазоне [{1}, {2}] -> {3}")
    @MethodSource("yearsWithSameTemplateCases")
    void getYearsWithSameTemplate_returnsAllMatchingYearsInRange(
            int year, int fromYear, int toYear, List<Integer> expectedYears) {
        assertThat(service.getYearsWithSameTemplate(year, fromYear, toYear)).isEqualTo(expectedYears);
    }

    static Stream<Arguments> yearsWithSameTemplateCases() {
        return Stream.of(
                Arguments.of(2023, 2015, 2040, List.of(2017, 2023, 2034)),
                Arguments.of(2023, 2030, 2036, List.of(2034)),
                Arguments.of(2023, 2040, 2015, List.of(2017, 2023, 2034)),
                Arguments.of(2023, 2025, 2026, List.of())
        );
    }
}
