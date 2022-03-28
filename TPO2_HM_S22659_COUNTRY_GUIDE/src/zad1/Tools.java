package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.stream.Collectors;

public class Tools {

    public static String readFromURL(String stringURL) {

        String text = "";
        try {
            URL url = new URL(stringURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();

            text = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            connection.disconnect();
        }catch (IOException e){
            e.printStackTrace();
        }
        return text;
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
