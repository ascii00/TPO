/**
 *
 *  @author Hrynevich Maksim S22659
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatClientTask implements Runnable {
    private final ChatClient clientChat;
    private final List<String> lines;
    private final int wait;

    public ChatClientTask(ChatClient client, List<String> reqs, int time) {
        this.clientChat = client;
        this.lines = reqs;
        this.wait = time;
    }

    public void get() throws InterruptedException, ExecutionException{
    }

    public static ChatClientTask create(ChatClient client, List<String> reqs, int time) {
        return new ChatClientTask(client, reqs, time);
    }

    public ChatClient getClient() {
        return clientChat;
    }

    @Override
    public void run() {
        try {
            clientChat.login();
            if (wait != 0) Thread.sleep(wait);
            for (String line : lines) {
                clientChat.send(line);
                if (wait != 0) Thread.sleep(wait);
            }
            clientChat.logout();
            if (wait != 0) Thread.sleep(wait);
        } catch (InterruptedException e) {

        }
    }
}