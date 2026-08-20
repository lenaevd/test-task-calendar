public class CalendarPrinter {
    private static final String WEEK_DAYS_STRING = "ПН ВТ СР ЧТ ПТ СБ ВС";
    private static final int CALENDAR_LENGTH = WEEK_DAYS_STRING.length();
    private static final int DAYS_IN_WEEK = 7;
    private static final int CELL_LENGTH = 2;
    private static final String[] MONTH_NAMES = {"ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ",
            "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"};

    public void printCalendar(CalendarTemplate calendarTemplate, int year) {
        System.out.print(createCalendarString(calendarTemplate, year));
    }

    private String createCalendarString(CalendarTemplate calendarTemplate, int year) {
        StringBuilder sb = new StringBuilder();
        for (MonthLayout month : calendarTemplate.months()) {
            appendMonth(sb, month, year);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private void appendMonth(StringBuilder sb, MonthLayout month, int year) {
        appendHeader(sb, month, year);
        sb.append(System.lineSeparator());
        sb.append(WEEK_DAYS_STRING).append(System.lineSeparator());
        appendGrid(sb, month);
    }

    private void appendHeader(StringBuilder sb, MonthLayout month, int year) {
        String monthName = MONTH_NAMES[month.month().getValue() - 1];
        String yearStr = String.valueOf(year);
        int spaceNumber = CALENDAR_LENGTH - yearStr.length() - monthName.length();

        sb.append(monthName);
        sb.append(" ".repeat(spaceNumber));
        sb.append(yearStr);
    }

    private void appendGrid(StringBuilder sb, MonthLayout month) {
        int column = 0;
        int firstDayColumnIndex = month.firstDayColumnIndex();
        for (int i = 0; i < firstDayColumnIndex; i++) {
            appendCellContent(sb, null);
            column = appendSeparator(sb, column);
        }
        for (int day = 1; day <= month.daysInMonth(); day++) {
            appendCellContent(sb, day);
            column = appendSeparator(sb, column);
        }
        if (column != 0) {
            sb.append(System.lineSeparator());
        }
    }

    private void appendCellContent(StringBuilder sb, Integer day) {
        if (day == null) {
            sb.append(" ".repeat(CELL_LENGTH));
        } else {
            if (day < 10) {
                sb.append(' ');
            }
            sb.append(day);
        }
    }

    private int appendSeparator(StringBuilder sb, int column) {
        int nextColumn = (column + 1) % DAYS_IN_WEEK;
        sb.append(nextColumn == 0 ? System.lineSeparator() : ' ');
        return nextColumn;
    }
}
