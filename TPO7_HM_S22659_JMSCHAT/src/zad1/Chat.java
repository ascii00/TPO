package zad1;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Hashtable;

public class Chat {

    private Context context;
    private ConnectionFactory connectionFactory;
    private Destination destination;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private String nick;
    private TextMessage message;
    private MessageListener listener;


    public Chat(MessageListener listener, String nick) throws JMSException, NamingException {
        this.nick = nick;
        this.listener = listener;
        context = getInitialContext();

        connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        destination = (Destination) context.lookup("topic1");
        connection = connectionFactory.createConnection();

        Subscribe();
        Publish();
    }

    public void Subscribe() throws JMSException {
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        consumer = session.createConsumer(destination);
        consumer.setMessageListener(listener);
    }

    public void Publish() throws JMSException{
        producer = session.createProducer(destination);
        connection.start();
        message = session.createTextMessage();
        message.setText(nick + " joined the chat!");
        producer.send(message);
    }

    public void sendMessage(String text) throws JMSException {
        message.setText("["+nick+"]: " + text);
        producer.send(message);
    }

    public Context getInitialContext() throws NamingException{
        Hashtable<String, String> properties = new Hashtable<>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
        return new InitialContext(properties);
    }

    public void disconnect() throws JMSException {
        message.setText(nick + " disconnected!");
        producer.send(message);
        if (connection != null) {
            try {
                session.close();
                connection.close();
                context.close();
            } catch (JMSException | NamingException ex) {
                ex.printStackTrace();
            }
        }
    }
}

























