package zad1;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

public class PassTimeSamples {
  
  static void say(Object ...s) {
    if (s.length == 0) System.out.println();
    else System.out.println(s[0]);
  }
  
  public static void main(String[] args) {
    // Parsowanie

    LocalDate ld1 = LocalDate.parse("2010-01-01");
    LocalDate ld2 = LocalDate.parse("2020-02-09");
    LocalDateTime ldt1 = LocalDateTime.parse("2020-03-27T12:15");
    LocalDateTime ldt2 = LocalDateTime.parse("2020-03-28T12:15");
    LocalDateTime ldt3 = LocalDateTime.parse("2020-03-29T12:15");
    
    // Zliczanie dni, miesięcy, lat
    System.out.println("222 " + ChronoUnit.YEARS.between(ld1, ld2) );
    say( ChronoUnit.MONTHS.between(ld1, ld2) );
    say( ChronoUnit.DAYS.between(ld1, ld2) );
    
//    System.exit(0);
    say();
    // To powie jak trzeba
    byPeriod(ld1, ld2);
    say();
//    System.exit(0);
    // czas
    say( ChronoUnit.HOURS.between(ldt1, ldt2) );
    say( ChronoUnit.HOURS.between(ldt2, ldt3) );
    ZonedDateTime zdt1 = ZonedDateTime.of(ldt1, ZoneId.of("Europe/Warsaw"));
    ZonedDateTime zdt2 = ZonedDateTime.of(ldt2, ZoneId.of("Europe/Warsaw"));
    ZonedDateTime zdt3 = ZonedDateTime.of(ldt3, ZoneId.of("Europe/Warsaw"));
    say();
    say( ChronoUnit.HOURS.between(zdt1, zdt2) );
    say( ChronoUnit.HOURS.between(zdt2, zdt3) );
    
    // Formatowanie
    String dpatt = "d MMMM yyyy (EEEE)";
    String tpatt = "d MMMM yyyy (EEEE) 'godz.' HH:mm";
    Locale pl = new Locale("pl");
    say();
    say (
        ld1.format( DateTimeFormatter.ofPattern(dpatt) )
    );
    say (
        ldt1.format( DateTimeFormatter.ofPattern(tpatt) )
    );

    say();
    // Tygodnie (z ułamkami)
    Locale none = new Locale("xx");
    say ( String.format(none,"%.2f", ChronoUnit.DAYS.between(ld1, ld2)/7.0));

  }
  
  static void byPeriod(LocalDate d1, LocalDate d2) {
    Period p = Period.between(d1, d2);
    System.out.println("111   " + p.getYears() );
    say( p.getMonths() );
    say( p.getDays() );
  }

}
