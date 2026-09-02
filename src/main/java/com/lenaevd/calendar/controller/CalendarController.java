package com.lenaevd.calendar.controller;

import com.lenaevd.calendar.dto.CalendarDto;
import com.lenaevd.calendar.dto.CalendarTextResponse;
import com.lenaevd.calendar.dto.MonthDto;
import com.lenaevd.calendar.dto.MonthTextResponse;
import com.lenaevd.calendar.dto.NextSameTemplateResponse;
import com.lenaevd.calendar.dto.SameTemplateYearsResponse;
import com.lenaevd.calendar.mapper.CalendarMapper;
import com.lenaevd.calendar.service.CalendarService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/calendars")
@Validated
@Tag(name = "Calendars", description = "Получение календаря для года или отдельного месяца, " +
        "как в формате JSON, так и в виде готовой строки")
public class CalendarController {

    private static final int MIN_YEAR = 1600;
    private static final int MAX_YEAR = Year.MAX_VALUE;
    private static final int MAX_YEAR_RANGE = 10000000;

    private static final int MIN_MONTH_NUMBER = 1;
    private static final int MAX_MONTH_NUMBER = 12;

    private final CalendarService calendarService;
    private final CalendarMapper calendarMapper;

    public CalendarController(CalendarService calendarService, CalendarMapper calendarMapper) {
        this.calendarService = calendarService;
        this.calendarMapper = calendarMapper;
    }

    @Operation(summary = "Получить календарь года (JSON)",
            description = "Возвращает год целиком: високосность, день недели 1 января и все 12 месяцев.")
    @GetMapping("/{year}")
    public ResponseEntity<CalendarDto> getCalendar(
            @Parameter(description = "Год", example = "2026")
            @PathVariable @Min(MIN_YEAR) @Max(MAX_YEAR) int year) {
        CalendarDto dto = calendarMapper.toCalendarDto(calendarService.getOrCreateTemplate(year), year);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Получить календарь года (текст)",
            description = "Возвращает год в виде отрендеренной строки.")
    @GetMapping("/{year}/text")
    public ResponseEntity<CalendarTextResponse> getCalendarText(
            @Parameter(description = "Год", example = "2026")
            @PathVariable @Min(MIN_YEAR) @Max(MAX_YEAR) int year) {
        CalendarTextResponse response = new CalendarTextResponse(calendarService.getRenderedCalendar(year));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить месяц (JSON)",
            description = "Возвращает конкретный месяц конкретного года со списком дней недели по датам.")
    @GetMapping("/{year}/months/{number}")
    public ResponseEntity<MonthDto> getMonth(
            @Parameter(description = "Год", example = "2026")
            @PathVariable @Min(MIN_YEAR) @Max(MAX_YEAR) int year,
            @Parameter(description = "Номер месяца", example = "3")
            @PathVariable @Min(MIN_MONTH_NUMBER) @Max(MAX_MONTH_NUMBER) int number) {
        MonthDto dto = calendarMapper.toMonthDto(calendarService.getMonth(year, number), year);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Получить месяц (текст)",
            description = "Возвращает конкретный месяц конкретного года в виде отрендеренной строки.")
    @GetMapping("/{year}/months/{number}/text")
    public ResponseEntity<MonthTextResponse> getMonthText(
            @Parameter(description = "Год", example = "2026")
            @PathVariable @Min(MIN_YEAR) @Max(MAX_YEAR) int year,
            @Parameter(description = "Номер месяца", example = "3")
            @PathVariable @Min(MIN_MONTH_NUMBER) @Max(MAX_MONTH_NUMBER) int number) {
        String renderedMonth = calendarService.getRenderedMonth(year, number);
        return ResponseEntity.ok(new MonthTextResponse(renderedMonth));
    }


    @Operation(summary = "Получить ближайший год с таким же шаблоном",
            description = "Ищет ближайший следующий год, совпадающий с данным по календарю (то есть одинаковый шаблон).")
    @GetMapping("/{year}/next-same")
    public ResponseEntity<NextSameTemplateResponse> getNextYearWithSameTemplate(
            @Parameter(description = "Год, для которого ищем ближайший аналогичный", example = "2026")
            @PathVariable @Min(MIN_YEAR) @Max(MAX_YEAR) int year) {
        NextSameTemplateResponse response = new NextSameTemplateResponse(
                year, calendarService.findNextYearWithSameTemplate(year));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить все годы с таким же шаблоном в диапазоне",
            description = "Возвращает годы из диапазона [from, to], у которых идентичны шаблоны.")
    @GetMapping("/{year}/same-templates")
    public ResponseEntity<SameTemplateYearsResponse> getYearsWithSameTemplate(
            @Parameter(description = "Год, для которого ищем аналогичные", example = "2026")
            @PathVariable @Min(MIN_YEAR) @Max(MAX_YEAR) int year,
            @Parameter(description = "Начало диапазона поиска", example = "2020")
            @RequestParam @Min(MIN_YEAR) @Max(MAX_YEAR_RANGE) int from,
            @Parameter(description = "Конец диапазона поиска", example = "2040")
            @RequestParam @Min(MIN_YEAR) @Max(MAX_YEAR_RANGE) int to) {
        List<Integer> list = calendarService.getYearsWithSameTemplate(year, from, to);
        return ResponseEntity.ok(new SameTemplateYearsResponse(from, to, list));
    }

    @Hidden
    @GetMapping(value = "/{year}/tt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getRenderedYear(
            @PathVariable @Min(MIN_YEAR) @Max(MAX_YEAR) int year) {
        return calendarService.getRenderedCalendar(year);
    }

    @Hidden
    @GetMapping(path = "/{year}/months/{number}/tt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getMonthTextPlain(
            @PathVariable @Min(MIN_YEAR) @Max(MAX_YEAR) int year,
            @PathVariable @Min(MIN_MONTH_NUMBER) @Max(MAX_MONTH_NUMBER) int number) {
        return calendarService.getRenderedMonth(year, number);
    }
}