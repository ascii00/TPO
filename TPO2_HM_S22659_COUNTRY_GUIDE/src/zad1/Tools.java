package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.stream.Collectors;

public class Tools {

    public static String readFromURL(String URL) {
        String response = null;
        try(BufferedReader reader =
                    new BufferedReader(new InputStreamReader(new URL(URL)
                            .openConnection()
                            .getInputStream()))){
            response = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    static public String getCountryCode(String country) {
        for (String iso : Locale.getISOCountries()) {
            Locale locale = new Locale("", iso);

            if (locale.getDisplayCountry().equals(country))
                return iso;
        }
        return null;
    }

    static public String getCurrencyCode(String country) {
        for (Locale locale: Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry().equals(country)) {
                Currency currency = Currency.getInstance(locale);

                return currency.getCurrencyCode();
            }
        }
        return null;
    }
}
