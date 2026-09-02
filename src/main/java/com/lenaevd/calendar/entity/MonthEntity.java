package com.lenaevd.calendar.entity;

import com.lenaevd.calendar.domain.Day;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "months",
        uniqueConstraints = @UniqueConstraint(name = "uq_month",
                columnNames = {"calendar_template_id", "month_number"}))
@Getter
@NoArgsConstructor
public class MonthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "calendar_template_id", nullable = false)
    private CalendarTemplateEntity calendarTemplate;

    @Column(name = "month_number", nullable = false)
    private int number;

    @Column(name = "days_count", nullable = false)
    private int daysCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "first_day_of_week", nullable = false, length = 9)
    private DayOfWeek firstDayOfWeek;

    public MonthEntity(CalendarTemplateEntity calendarTemplate, int number, int daysCount, DayOfWeek firstDayOfWeek) {
        this.calendarTemplate = calendarTemplate;
        this.number = number;
        this.daysCount = daysCount;
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public List<Day> getDaysList() {
        List<Day> days = new ArrayList<>();
        DayOfWeek dayOfWeek = firstDayOfWeek;
        for (int i = 1; i <= daysCount; i++) {
            days.add(new Day(MonthDay.of(Month.of(number), i), dayOfWeek));
            dayOfWeek = dayOfWeek.plus(1);
        }
        return days;
    }
}
