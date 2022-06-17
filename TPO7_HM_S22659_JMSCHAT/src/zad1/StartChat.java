package zad1;

import javax.swing.*;

public class StartChat {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                ChatWindow::new
        );
    }
}
