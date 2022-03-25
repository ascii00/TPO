/**
 *
 *  @author Hrynevich Maksim S22659
 *
 */

package zad1;


import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();

    System.out.println(weatherJson);
    System.out.println(rate1);
    System.out.println(rate2);

    SwingUtilities.invokeLater(GUI::new);
  }
}
