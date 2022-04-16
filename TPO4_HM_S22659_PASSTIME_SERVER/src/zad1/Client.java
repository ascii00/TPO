/**
 *
 *  @author Hrynevich Maksim S22659
 *
 */

package zad1;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.channels.SocketChannel;
import java.nio.CharBuffer;
import java.net.InetSocketAddress;

public class Client{

    private SocketChannel socketChannel;
    private final String id;
    private final String host;
    private final int port;

    public final Charset charsetUtf8 = StandardCharsets.UTF_8;

    public Client(String host, int port, String id){
        this.id=id;
        this.port=port;
        this.host=host;
    }

    public String getId() {
        return id;
    }

    public void connect(){
        try{
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String send(String req){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(req.getBytes().length);
        try {
            byteBuffer.put(charsetUtf8.encode(req));
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteBuffer byteBufferTwo = ByteBuffer.allocateDirect(1024);
        StringBuilder stringBuilder = new StringBuilder();

        try {
            int reder;
            while(true) {
                if ((reder = socketChannel.read(byteBufferTwo)) >= 1)
                    break;
            }
            while( reder > 0){
                reder = socketChannel.read(byteBufferTwo);
                byteBufferTwo.flip();
                CharBuffer charBuffer = charsetUtf8.decode(byteBufferTwo);
                stringBuilder.append(charBuffer);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
