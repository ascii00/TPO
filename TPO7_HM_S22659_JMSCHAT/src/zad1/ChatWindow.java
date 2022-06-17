package zad1;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ChatWindow extends JFrame implements MessageListener {

    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final JPanel topPanel = new JPanel();
    private final JPanel bottomPanel = new JPanel();
    private final JButton connectButton = new JButton("Connect");
    private final JButton disconnectButton = new JButton("Disconnect");
    private final JButton sendButton = new JButton("Send");
    private final JTextField message = new JTextField(50);
    private final JTextArea messageArea = new JTextArea(10, 20);
    private final JScrollPane messageAreaScrollPane = new JScrollPane(messageArea);
    private String nickName;
    private Chat chat;

    public ChatWindow() {

        setConnectButton();
        setDisconnectButton();
        setSendButton();

        message.setEnabled(false);
        disconnectButton.setEnabled(false);
        topPanel.add(connectButton);
        topPanel.add(disconnectButton);

        // Center content
        messageArea.setEditable(false);
        messageAreaScrollPane.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, Color.DARK_GRAY), "Chat"));

        // Bottom content
        sendButton.setEnabled(false);

        bottomPanel.add(message);
        bottomPanel.add(sendButton);

        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(messageAreaScrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
        add(mainPanel);

        setTitle("Chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700,450);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String showNickNameDialog(){
        return JOptionPane.showInputDialog(
                mainPanel
                , "Enter your nick:"
                , "Connection"
                , JOptionPane.WARNING_MESSAGE
        );
    }

    private void setDisconnectButton(){
        disconnectButton.addActionListener(e -> {
            try {
                chat.disconnect();
            } catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
            message.setEnabled(false);
            connectButton.setEnabled(true);
            disconnectButton.setEnabled(false);
            sendButton.setEnabled(false);
        });
    }

    private void setConnectButton(){
        connectButton.addActionListener(e -> {
            do {
                nickName = showNickNameDialog();
                if (nickName == null) return;
            } while (nickName.equals(""));

            message.setEnabled(true);
            connectButton.setEnabled(false);
            disconnectButton.setEnabled(true);
            sendButton.setEnabled(true);
            try {
                chat = new Chat(this, nickName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setSendButton(){
        sendButton.addActionListener(e -> {
            if (!message.getText().equals("")) {
                try {
                    chat.sendMessage(message.getText());
                    message.setText("");
                } catch (JMSException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public void onMessage(Message message) {
        try{
            messageArea.append(((TextMessage) message).getText() + "\n");
        }catch (JMSException e){
            e.printStackTrace();
        }
    }
}
