package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    JPanel panelCountry = new JPanel();
    JPanel panelCity = new JPanel();
    JPanel panelCurrency = new JPanel();
    JPanel panelInput = new JPanel();
    JPanel panelMain = new JPanel();
    JPanel panelWeather = new JPanel();
    JPanel panelRate = new JPanel();
    JPanel panelRateToPLN = new JPanel();


    JButton submitButton = new JButton("Submit");

    JTextField jTextFieldCountry = new JTextField(15);
    JTextField jTextFieldCity = new JTextField(15);
    JTextField jTextFieldCurrency = new JTextField(15);

    JLabel jLabelCountry = new JLabel("Country:");
    JLabel jLabelCity = new JLabel("City:");
    JLabel jLabelCurrency = new JLabel("Currency:");
    JLabel jLabelWeather = new JLabel("Weather:");
    JLabel jLabelRate = new JLabel("Exchange rate:");
    JLabel jLabelRateToPLN = new JLabel("Exchange rate to PLN:");

    JTextArea jTextAreaWeather = new JTextArea();
    JTextArea jTextAreaRate = new JTextArea();
    JTextArea jTextAreaRateToPLN = new JTextArea();
    JScrollPane jScrollPane = new JScrollPane(jTextAreaWeather);

    public GUI() {
        setTitle("Country guide");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jTextAreaWeather.setEditable(false);
        jTextAreaWeather.setColumns(55);
        jTextAreaWeather.setRows(3);

        jTextAreaRate.setEditable(false);
        jTextAreaRate.setColumns(13);
        jTextAreaRate.setRows(1);

        jTextAreaRateToPLN.setEditable(false);
        jTextAreaRateToPLN.setColumns(6);
        jTextAreaRateToPLN.setRows(1);

        panelCountry.setLayout(new FlowLayout());
        panelCountry.add(jLabelCountry, FlowLayout.LEFT);
        panelCountry.add(jTextFieldCountry, FlowLayout.CENTER);

        panelCity.setLayout(new FlowLayout());
        panelCity.add(jLabelCity, FlowLayout.LEFT);
        panelCity.add(jTextFieldCity, FlowLayout.CENTER);

        panelCurrency.setLayout(new FlowLayout());
        panelCurrency.add(jLabelCurrency, FlowLayout.LEFT);
        panelCurrency.add(jTextFieldCurrency, FlowLayout.CENTER);

        panelInput.setLayout(new BorderLayout());
        panelInput.add(panelCountry, BorderLayout.LINE_START);
        panelInput.add(panelCity, BorderLayout.CENTER);
        panelInput.add(panelCurrency, BorderLayout.LINE_END);

        panelWeather.setLayout(new FlowLayout());
        panelWeather.add(jLabelWeather, FlowLayout.LEFT);
        panelWeather.add(jScrollPane, FlowLayout.CENTER);

        panelCurrency.setLayout(new FlowLayout());
        panelCurrency.add(jLabelCurrency, FlowLayout.LEFT);

        panelRate.add(jLabelRate, FlowLayout.LEFT);
        panelRate.add(jTextAreaRate, FlowLayout.CENTER);

        panelRateToPLN.add(jLabelRateToPLN, FlowLayout.LEFT);
        panelRateToPLN.add(jTextAreaRateToPLN, FlowLayout.CENTER);

        JFXPanel jfxPanel = new JFXPanel();
        panelMain.add(jfxPanel);

        panelMain.add(panelInput);
        panelMain.add(submitButton);
        panelMain.add(panelWeather);
        panelMain.add(panelRate);
        panelMain.add(panelRateToPLN);
        panelMain.add(jfxPanel);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (jTextFieldCity.getText().equals("") || jTextFieldCountry.getText().equals("")) {
                    System.err.println("Podaj miasto i kraj!");
                    return;
                }
                jTextAreaWeather.setText("");
                jTextAreaRate.setText("");
                jTextAreaRateToPLN.setText("");

                try {

                    Service service = new Service(jTextFieldCountry.getText());
                    jTextAreaWeather.append(service.getWeather(jTextFieldCity.getText()));
                    if (!jTextFieldCurrency.getText().equals("")) {
                        jTextAreaRate.append(String.valueOf(service.getRateFor(jTextFieldCurrency.getText())));
                        jTextAreaRate.append(" " + service.currencyCode + " in 1 " + jTextFieldCurrency.getText());
                        jTextAreaRateToPLN.append(String.valueOf(service.getNBPRate()));
                    }
                    Platform.runLater(() -> {
                        WebView webView = new WebView();
                        jfxPanel.setScene(new Scene(webView));
                        webView.getEngine().load("https://en.wikipedia.org/wiki/" + jTextFieldCity.getText());
                    });
                }
                catch (Exception ex){
                    System.err.println("Wrong city or country!");
                }
            }
        });

        add(panelMain);
        setResizable(true);
        setSize(850,850);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
