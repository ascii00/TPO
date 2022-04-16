/**
 *
 *  @author Hrynevich Maksim S22659
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.*;

public class Server {

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final StringBuilder log;

    volatile boolean serverStatus;

    private static final Charset charset = StandardCharsets.UTF_8;

    private final Map<SocketChannel,String> clientNames;
    private final Map<SocketChannel,String> clientLogs;


    public Server(String host,int port) throws IOException{
        clientNames = new HashMap<>();
        clientLogs = new HashMap<>();
        log = new StringBuilder();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(host,port));

        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public String getServerLog() {
        return log.toString();
    }

    public void stopServer() {
        serverStatus = false;
    }

    public void startServer(){
        new Thread(()-> {
            serverStatus = true;

            while(serverStatus) {
                try {
                    selector.select();
                    Set<SelectionKey> selectionKeySet = selector.selectedKeys();

                    Iterator<SelectionKey> selectionKeyIterator = selectionKeySet.iterator();
                    while (selectionKeyIterator.hasNext()){
                        SelectionKey key = selectionKeyIterator.next();
                        selectionKeyIterator.remove();

                        if(key.isAcceptable()){
                            SocketChannel clientChannel = serverSocketChannel.accept();
                            clientChannel.configureBlocking(false);
                            clientChannel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            continue;
                        }
                        if(key.isReadable()){
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            serviceRequest(clientChannel);
                        }
                    }
                }catch (IOException ignored){}
            }
        }).start();
    }

    private void serviceRequest(SocketChannel socketChannel) {
        if (!socketChannel.isOpen()) return;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        byteBuffer.clear();

        try {
            for (int bytesRead = socketChannel.read(byteBuffer); bytesRead > 0; bytesRead = socketChannel.read(byteBuffer)) {
                byteBuffer.flip();
                CharBuffer charBuffer = charset.decode(byteBuffer);
                stringBuilder.append(charBuffer);
            }
            requestOperation(socketChannel, stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestOperation(SocketChannel socketChannel, String request) throws IOException {

        StringBuilder response = new StringBuilder();

        if(request.contains("login")){

            String cmd = "logged in";
            response.append(cmd);
            clientNames.put(socketChannel, request.substring(request.indexOf(' ')+1));
            log
                    .append(clientNames.get(socketChannel))
                    .append(" logged in at ")
                    .append(LocalTime.now())
                    .append('\n');
            clientLogs.put(socketChannel,"=== "+clientNames.get(socketChannel)+ " log start ==="+'\n'+"logged in"+'\n');

        }else if(request.equals("bye")){

            String cmd = "logged out";
            response.append(cmd);
            log
                    .append(clientNames.get(socketChannel))
                    .append(" logged out at ")
                    .append(LocalTime.now())
                    .append('\n');
            clientLogs.put(socketChannel,clientLogs.get(socketChannel)+cmd+'\n'+"=== "+clientNames.get(socketChannel)+ " log end ==="+'\n');

        }else if(request.equals("bye and log transfer")){

            String str = "logged out";
            clientLogs.put(socketChannel,clientLogs.get(socketChannel)+str+'\n'+"=== "+clientNames.get(socketChannel)+ " log end ==="+'\n');
            log
                    .append(clientNames.get(socketChannel))
                    .append(" logged out at ")
                    .append(LocalTime.now())
                    .append('\n');
            response.append(clientLogs.get(socketChannel));

        }else{

            String result = Time.passed(request.substring(0, request.indexOf(' ')), request.substring(request.indexOf(' ')+1));
            response.append(result);
            log
                    .append(clientNames.get(socketChannel))
                    .append(" request at ")
                    .append(LocalTime.now())
                    .append(": \"")
                    .append(request)
                    .append("\"")
                    .append('\n');
            clientLogs.put(socketChannel,clientLogs.get(socketChannel)+"Request: "+ request+'\n'+"Result: "+'\n'+result+'\n');
        }

        ByteBuffer answer = ByteBuffer.allocateDirect(response.toString().getBytes().length);

        answer.put(charset.encode(response.toString()));
        answer.flip();
        socketChannel.write(answer);
    }
}
