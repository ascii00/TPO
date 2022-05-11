/**
 *
 *  @author Hrynevich Maksim S22659
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ChatClient extends Thread {

    private SocketChannel socketChannel;
    private final StringBuilder view;
    private final String id;
    private final Charset charset = Charset.forName("ISO-8859-2");

    public ChatClient(String host, int port, String id) {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
        } catch (IOException e) {

        }
        this.id = id;
        this.view = new StringBuilder("=== ")
                .append(id)
                .append(" chat view")
                .append("\n");
    }

    public void login() {
        this.start();
        this.send("logged in");
    }

    public void logout() throws InterruptedException {
        this.send("logged out");
        Thread.sleep(75);
        this.interrupt();
    }

    public void send(String req) {
        try {
            Thread.sleep(75);
            socketChannel.write(charset.encode(id + ": " + req + "\n"));
        } catch (IOException e) {
            send(req);
        } catch (InterruptedException e) {

        }
    }

    public String getChatView() {
        return view.toString();
    }

    @Override
    public void run() {
        try {

            while (!isInterrupted()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(256);
                StringBuilder requestString = new StringBuilder();

                while (socketChannel.read(byteBuffer) > 0) {
                    byteBuffer.flip();
                    requestString.append(charset.decode(byteBuffer));
                    byteBuffer.clear();
                }
                if (!requestString.toString().isEmpty())
                    view.append(requestString);
            }

        } catch (IOException e) {

        }
    }
}