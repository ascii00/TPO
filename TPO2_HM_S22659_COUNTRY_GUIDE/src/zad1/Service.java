/**
 *
 *  @author Hrynevich Maksim S22659
 *
 */

package zad1;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Service {

    String country;
    String countryCode;
    String currencyCode;

    public Service(String country) {
        this.country = country;
        this.countryCode = Tools.getCountryCode(country);
        this.currencyCode = Tools.getCurrencyCode(country);
    }

    public String getWeather(String city){
        String apiURL = "https://api.openweathermap.org/data/2.5/weather?q=";
        String apiKEY = "&appid=37fe87935932706aee66543634487df3";

        String weatherInfoAddress = apiURL + city + "," + countryCode + apiKEY;

        return Tools.readFromURL(weatherInfoAddress);
    }

    public double getRateFor(String currency){
        String currencyInfoAddress = "https://api.exchangerate.host/convert?from="
                + currencyCode
                + "&to="
                + currency;
        String response = Tools.readFromURL(currencyInfoAddress);

        JsonParser parser = new JsonParser();
        JsonObject parse = (JsonObject) parser.parse(response);
        JsonObject parseMainPart = (JsonObject) parse.get("info");

        return Double.parseDouble(String.valueOf(parseMainPart.get("rate")));
    }

    public Double getNBPRate(){

        if(currencyCode.equals("PLN"))
            return 1d;

        String currencyListOne = "https://www.nbp.pl/kursy/xml/a054z220318.xml";
        String currencyListTwo = "https://www.nbp.pl/kursy/xml/b011z220316.xml";

        String responseOne =  Tools.readFromURL(currencyListOne);
        String responseTwo = Tools.readFromURL(currencyListTwo);

        Pattern codePattern = Pattern.compile("<kod_waluty>"+currencyCode+"</kod_waluty>");
        Matcher matcherOne = codePattern.matcher(responseOne);
        Matcher matcherTwo = codePattern.matcher(responseTwo);

        String trueResponse;

        if (matcherOne.find())
            trueResponse = responseOne;
        else if (matcherTwo.find())
            trueResponse = responseTwo;
        else
            return null;

        Pattern currencyPattern = Pattern.compile("<kod_waluty>"
                + currencyCode
                + "</kod_waluty>\\s*<kurs_sredni>[0-9,]{6}");
        Matcher currencyMatcher = currencyPattern.matcher(trueResponse);
        if (currencyMatcher.find()) {
            String result = currencyMatcher.group();
            result = result.substring(result.length() - 6).replaceAll(",", ".");

            return Double.parseDouble(result);
        }
        return null;
    }
}

