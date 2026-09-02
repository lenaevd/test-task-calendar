package com.lenaevd.calendar.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "calendar_templates",
        uniqueConstraints = @UniqueConstraint(name = "uq_calendar", columnNames = {"first_day_of_week", "is_leap"}))
@Getter
@NoArgsConstructor
public class CalendarTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "first_day_of_week", nullable = false, length = 9)
    private DayOfWeek firstDayOfWeek;

    @Column(name = "is_leap", nullable = false)
    private boolean isLeap;

    @OneToMany(mappedBy = "calendarTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private List<MonthEntity> months = new ArrayList<>();

    public CalendarTemplateEntity(DayOfWeek firstDayOfWeek, boolean isLeap) {
        this.firstDayOfWeek = firstDayOfWeek;
        this.isLeap = isLeap;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CalendarTemplateEntity that)) return false;
        return isLeap == that.isLeap && firstDayOfWeek == that.firstDayOfWeek;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstDayOfWeek, isLeap);
    }
}