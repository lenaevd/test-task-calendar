import java.util.Scanner;

public class CalendarApp {
    private static final CalendarService service = new CalendarService();
    private static final CalendarPrinter printer = new CalendarPrinter();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (true) {
            if (input.hasNextInt()) {
                int year = input.nextInt();
                if (year == 0) {
                    System.out.println("Завершение программы");
                    break;
                }
                try {
                    getAndPrintCalendar(year);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Введите число");
                input.next();
            }
        }
    }

    private static void getAndPrintCalendar(int year) {
        validate(year);
        CalendarTemplate template = service.getCalendarTemplate(year);
        printer.printCalendar(template, year);
    }

    private static void validate(int year) {
        if (year < 1600) {
            throw new IllegalArgumentException("Введите год, начиная с 1600");
        }
        if (year > 2126) {
            throw new IllegalArgumentException("Введите год, меньший чем 2127");
        }
    }
}
