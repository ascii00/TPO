/**
 *
 *  @author Hrynevich Maksim S22659
 *
 */

package zad1;


import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time {

    public static String passed(String from, String to){
        Locale.setDefault(Locale.ENGLISH);
        try {
            if (getTimeFormat(from))
                return getPrintLine(LocalDate.parse(from), LocalDate.parse(to));
            else
                return getPrintLine(LocalDateTime.parse(from), LocalDateTime.parse(to));
        }catch (DateTimeParseException e){
            return "*** java.time.format.DateTimeParseException: "  + e.getMessage();
        }
    }

    private static String getPrintLine(LocalDate dateFrom, LocalDate dateTo){

        Period period = Period.between(dateFrom, dateTo);
        String result = "Od "
                + dateFrom.getDayOfMonth()
                + " "
                + getMonth(String.valueOf(dateFrom.getMonth()))
                + " "
                + dateFrom.getYear()
                + " ("
                + getDay(String.valueOf(dateFrom.getDayOfWeek()))
                + ") do "
                + dateTo.getDayOfMonth()
                + " "
                + getMonth(String.valueOf(dateTo.getMonth()))
                + " "
                + dateTo.getYear()
                + " ("
                + getDay(String.valueOf(dateTo.getDayOfWeek()))
                + ")\n- mija: "
                + ChronoUnit.DAYS.between(dateFrom, dateTo)
                + " dni, tygodni "
                + String.format("%.2f", ChronoUnit.DAYS.between(dateFrom, dateTo)/7.0);

        if (ChronoUnit.DAYS.between(dateFrom, dateTo) > 0){
            result += "\n- kalendarzowo: ";
            if (ChronoUnit.YEARS.between(dateFrom, dateTo) > 0){
                result+=ChronoUnit.YEARS.between(dateFrom, dateTo);
                if (ChronoUnit.YEARS.between(dateFrom, dateTo) == 1)
                    result+=" rok";
                else
                    result+=" lat";
                if (period.getMonths() > 0 || period.getDays() > 0)
                    result += ", ";
            }
            if (period.getMonths() > 0){
                result += period.getMonths();
                if (period.getMonths() == 1)
                    result += " miesiąc";
                else
                    result += " miesiące";
                if (period.getDays() > 0)
                    result += ", ";
            }
            if(period.getDays() > 0){
                result += period.getDays();
                if (period.getDays() == 1)
                    result += " dzień";
                else
                    result += " dni";
            }
        }

        System.out.println(result);
        return result;
    }

    private static String getPrintLine(LocalDateTime dateFrom, LocalDateTime dateTo){

        ZonedDateTime dateTimeFrom = ZonedDateTime.of(dateFrom, ZoneId.of("Europe/Warsaw"));
        ZonedDateTime dateTimeTo = ZonedDateTime.of(dateTo, ZoneId.of("Europe/Warsaw"));

        String minutesFrom = dateTimeFrom.getMinute() < 10 ? "0"+dateTimeFrom.getMinute() : ""+dateTimeFrom.getMinute();
        String minutesTo = dateTimeTo.getMinute() < 10 ? "0"+dateTimeTo.getMinute() : ""+dateTimeTo.getMinute();
        String result = "Od "
                + dateTimeFrom.getDayOfMonth()
                + " "
                + getMonth(String.valueOf(dateTimeFrom.getMonth()))
                + " "
                + dateTimeFrom.getYear()
                + " ("
                + getDay(String.valueOf(dateTimeFrom.getDayOfWeek()))
                + ") godz. "
                + dateTimeFrom.getHour()
                + ":"
                + minutesFrom
                + " do "
                + dateTimeTo.getDayOfMonth()
                + " "
                + getMonth(String.valueOf(dateTimeTo.getMonth()))
                + " "
                + dateTimeTo.getYear()
                + " ("
                + getDay(String.valueOf(dateTimeTo.getDayOfWeek()))
                + ") "
                + "godz. "
                + dateTimeFrom.getHour()
                + ":"
                + minutesTo
                + "\n- mija: "
                + ChronoUnit.DAYS.between(dateTimeFrom, dateTimeTo)
                + " dni, tygodni "
                + String.format("%.2f", ChronoUnit.DAYS.between(dateTimeFrom, dateTimeTo)/7.0)
                + "\n- godzin: "
                + ChronoUnit.HOURS.between(dateTimeFrom, dateTimeTo)
                + ", minut: "
                + ChronoUnit.MINUTES.between(dateTimeFrom, dateTimeTo);

        LocalDate localDateFrom = LocalDate.from(dateTimeFrom);
        LocalDate localDateTo = LocalDate.from(dateTimeTo);

        Period period = Period.between(localDateFrom, localDateTo);

        if (ChronoUnit.DAYS.between(dateTimeFrom, dateTimeTo) > 0){
            result += "\n- kalendarzowo: ";
            if (ChronoUnit.YEARS.between(dateTimeFrom, dateTimeTo) > 0){
                result+=ChronoUnit.YEARS.between(dateTimeFrom, dateTimeTo);
                if (ChronoUnit.YEARS.between(dateTimeFrom, dateTimeTo) == 1)
                    result+=" rok";
                else
                    result+=" lat";
                if (period.getMonths() > 0 || period.getDays() > 0)
                    result += ", ";
            }
            if (period.getMonths() > 0){
                result += period.getMonths();
                if (period.getMonths() == 1)
                    result += " miesiąc";
                else
                    result += " miesiące";
                if (period.getDays() > 0)
                    result += ", ";
            }
            if(period.getDays() > 0){
                result += period.getDays();
                if (period.getDays() == 1)
                    result += " dzień";
                else
                    result += " dni";
            }
        }


        return result;
    }

    private static boolean getTimeFormat(String line){
        Pattern pattern = Pattern.compile("[.+T]");
        Matcher matcher = pattern.matcher(line);

        return !matcher.find();
    }

    private static String getMonth(String month){
        switch (month){
            case "JANUARY" :
                return "stycznia";
            case "FEBRUARY" :
                return "lutego";
            case "MARCH" :
                return "marca";
            case "APRIL" :
                return "kwietnia";
            case "MAY" :
                return "maja";
            case "JUNE" :
                return "czerwca";
            case "JULY" :
                return "lipca";
            case "AUGUST" :
                return "sierpnia";
            case "SEPTEMBER" :
                return "września";
            case "OCTOBER" :
                return "października";
            case "NOVEMBER" :
                return "listopada";
        }
        return "grudnia";
    }

    private static String getDay(String day) {
        switch (day) {
            case "MONDAY":
                return "poniedziałek";
            case "TUESDAY":
                return "wtorek";
            case "WEDNESDAY":
                return "środa";
            case "THURSDAY":
                return "czwartek";
            case "FRIDAY":
                return "piątek";
            case "SATURDAY":
                return "sobota";
        }
        return "niedziela";
    }
}
