package com.lenaevd.calendar.repository;


import com.lenaevd.calendar.entity.CalendarTemplateEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Optional;

public interface CalendarTemplateRepository extends JpaRepository<CalendarTemplateEntity, Integer> {

    @EntityGraph(attributePaths = "months")
    Optional<CalendarTemplateEntity> findByFirstDayOfWeekAndIsLeap(DayOfWeek firstDayOfWeek, boolean isLeap);
}