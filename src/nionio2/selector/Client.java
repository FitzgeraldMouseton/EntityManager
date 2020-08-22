package nionio2.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {

    public static void main(String[] args) throws IOException {

        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 12345);
        SocketChannel socketChannel = SocketChannel.open(address);
        final Socket socket = socketChannel.socket();

        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("Hello\n").flip();
        socketChannel.write(StandardCharsets.UTF_8.encode(charBuffer));
        socket.close();
    }
}
