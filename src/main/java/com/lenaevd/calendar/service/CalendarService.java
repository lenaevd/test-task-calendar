package com.lenaevd.calendar.service;

import com.lenaevd.calendar.entity.CalendarTemplateEntity;
import com.lenaevd.calendar.entity.MonthEntity;

import java.util.List;

public interface CalendarService {
    /**
     * Возвращает календарь года из БД, если его нет, то создаёт
     * @param year год
     * @return сущность календаря
     */
    CalendarTemplateEntity getOrCreateTemplate(int year);

    /**
     * Возвращает календарь года в виде строки
     * @param year год
     * @return строку
     */
    String getRenderedCalendar(int year);

    /**
     * Возвращает месяц конкретного года
     * @param year год
     * @param monthNumber номер месяца
     * @return сущность месяца
     */
    MonthEntity getMonth(int year, int monthNumber);

    /**
     * Возвращает месяц конкретного года в виде строки
     * @param year год
     * @param monthNumber номер месяца
     * @return строку
     */
    String getRenderedMonth(int year, int monthNumber);

    /**
     * Находит для данного года следующий, совпадающий по календарю
     * @param year год
     * @return год
     */
    int findNextYearWithSameTemplate(int year);

    /**
     * Находит для данного года, годы, совпадающие по календарю, в заданном промежутке.
     * В случае, когда диапазон указан наоборот от большего года к меньшему, меняет их местами.
     * @param year год
     * @param fromYear год начало промежутка
     * @param toYear год конец промежутка
     * @return список годов, список может быть пустой,
     * а также список включает данный год, если он находится в диапазоне
     */
    List<Integer> getYearsWithSameTemplate(int year, int fromYear, int toYear);
}
