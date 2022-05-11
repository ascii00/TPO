/**
 *
 *  @author Hrynevich Maksim S22659
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;


public class ChatServer extends Thread {
    private static final HashMap<String, SocketChannel> clients = new HashMap<>();
    private final Charset charset = Charset.forName("ISO-8859-2");
    private final StringBuilder log;
    private ServerSocketChannel serverSocket;
    private Selector selector;

    public ChatServer(String host, int port) {
        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(host, port));
            serverSocket.configureBlocking(false);

            selector = Selector.open();
            serverSocket.register(selector, serverSocket.validOps());
        } catch (IOException e) {

        }
        this.log = new StringBuilder();
    }

    public void startServer() {
        this.start();
        System.out.println("Server started" + "\n");
    }

    public void stopServer() throws InterruptedException {
        Thread.sleep(2000);
        this.interrupt();
        System.out.println("Server stopped");
    }

    public String getServerLog() {
        return String.valueOf(log);
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                selector.select();

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocket.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }

                    if (key.isReadable()) {
                        SelectableChannel selectableChannel = key.channel();
                        SocketChannel socketChannel = (SocketChannel) selectableChannel;

                        if (socketChannel.isOpen()) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(256);
                            StringBuilder request = new StringBuilder();

                            while (socketChannel.read(byteBuffer) > 0) {
                                byteBuffer.flip();
                                request.append(charset.decode(byteBuffer));
                                byteBuffer.clear();
                            }

                            if (request.toString().contains("logged in"))
                                clients.put(request.substring(0, request.indexOf(":")), socketChannel);

                            if (request.toString().contains("logged out")) {
                                String ID = request.substring(0, request.toString().indexOf(":"));
                                clients.get(ID).write(charset.encode(request.toString().replace(": logged", " logged")));
                                clients.remove(ID);
                            }

                            request = new StringBuilder(request.toString().replace(": logged", " logged"));

                            if (!request.toString().isEmpty()) {
                                log
                                        .append(new SimpleDateFormat("HH:mm:ss.SSS").format(System.currentTimeMillis()))
                                        .append(" ")
                                        .append(request);

                                for (Map.Entry<String, SocketChannel> entry : clients.entrySet())
                                    entry.getValue().write(charset.encode(request.toString()));
                            }
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {

        }
    }
}