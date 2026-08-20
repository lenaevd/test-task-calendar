import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarService {

    private final Map<CalendarKey, CalendarTemplate> templates = new HashMap<>();

    public CalendarTemplate getCalendarTemplate(int year) {
        DayOfWeek firstDayOfWeek = LocalDate.of(year, 1, 1).getDayOfWeek();
        CalendarKey key = new CalendarKey(firstDayOfWeek, Year.isLeap(year));
        if (!templates.containsKey(key)) {
            CalendarTemplate template = buildTemplate(key, year);
            templates.put(key, template);
        }
        return templates.get(key);
    }

    private CalendarTemplate buildTemplate(CalendarKey key, int year) {
        List<MonthLayout> months = new ArrayList<>();
        for (Month month : Month.values()) {
            LocalDate first = LocalDate.of(year, month, 1);
            int daysInMonth = first.lengthOfMonth();
            int columnIndex = first.getDayOfWeek().getValue() - 1;
            months.add(new MonthLayout(month, daysInMonth, columnIndex));
        }
        return new CalendarTemplate(key, months);
    }
}
