package com.lenaevd.calendar.service;

import com.lenaevd.calendar.domain.CalendarKey;
import com.lenaevd.calendar.entity.CalendarTemplateEntity;
import com.lenaevd.calendar.entity.MonthEntity;
import com.lenaevd.calendar.repository.CalendarTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class CalendarServiceImpl implements CalendarService {

    private static final int MAX_SEARCH_YEARS = 50;
    private final CalendarTemplateRepository repository;
    private final CalendarRenderer renderer;

    public CalendarServiceImpl(CalendarTemplateRepository repository, CalendarRenderer renderer) {
        this.repository = repository;
        this.renderer = renderer;
    }

    @Override
    @Transactional
    public CalendarTemplateEntity getOrCreateTemplate(int year) {
        CalendarKey key = CalendarKey.of(year);
        return repository.findByFirstDayOfWeekAndIsLeap(key.firstDayOfWeek(),
                key.isLeap()).orElseGet(() -> repository.save(buildTemplate(year, key)));
    }

    @Override
    @Transactional
    public String getRenderedCalendar(int year) {
        CalendarTemplateEntity template = getOrCreateTemplate(year);
        return renderer.renderYear(year, template.getMonths());
    }

    @Override
    @Transactional
    public MonthEntity getMonth(int year, int monthNumber) {
        CalendarTemplateEntity template = getOrCreateTemplate(year);
        return template.getMonths().stream()
                .filter(m -> m.getNumber() == monthNumber)
                .findFirst()
                .orElseThrow();
    }

    @Override
    @Transactional
    public String getRenderedMonth(int year, int monthNumber) {
        return renderer.renderMonth(year, getMonth(year, monthNumber));
    }

    @Override
    public int findNextYearWithSameTemplate(int year) {
        CalendarKey origin = CalendarKey.of(year);
        for (int candidate = year + 1; candidate <= year + MAX_SEARCH_YEARS; candidate++) {
            if (CalendarKey.of(candidate).equals(origin)) {
                return candidate;
            }
        }
        return 0;
    }

    @Override
    public List<Integer> getYearsWithSameTemplate(int year, int fromYear, int toYear) {
        if (fromYear > toYear) {
            int y = toYear;
            toYear = fromYear;
            fromYear = y;
        }
        CalendarKey origin = CalendarKey.of(year);
        return IntStream.rangeClosed(fromYear, toYear)
                .filter(y -> CalendarKey.of(y).equals(origin))
                .boxed()
                .toList();
    }

//    private Map<CalendarKey, List<Integer>> groupYearsByTemplate(int fromYear, int toYear) {
//        if (fromYear > toYear) {
//            int y = toYear;
//            toYear = fromYear;
//            fromYear = y;
//        }
//        Map<CalendarKey, List<Integer>> groups = new LinkedHashMap<>();
//        for (int year = fromYear; year <= toYear; year++) {
//            groups.computeIfAbsent(CalendarKey.of(year), k -> new ArrayList<>()).add(year);
//        }
//        return groups;
//    }

    private CalendarTemplateEntity buildTemplate(int year, CalendarKey key) {
        CalendarTemplateEntity template = new CalendarTemplateEntity(key.firstDayOfWeek(), key.isLeap());
        List<MonthEntity> months = new ArrayList<>(12);
        for (Month month : Month.values()) {
            LocalDate firstDate = LocalDate.of(year, month, 1);
            months.add(new MonthEntity(template, month.getValue(), firstDate.lengthOfMonth(), firstDate.getDayOfWeek()));
        }
        template.setMonths(months);
        return template;
    }
}
