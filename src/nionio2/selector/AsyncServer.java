package nionio2.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class AsyncServer {
    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // делаем канал асинхронным
        serverSocketChannel.configureBlocking(false);

        // создаем сокет
        ServerSocket serverSocket = serverSocketChannel.socket();
        // назначаем сокету адрес
        serverSocket.bind(new InetSocketAddress(12345));

        // Создаем селектор
        Selector selector = Selector.open();
        // Регистрируем канал и сохраняем на будущее ключ
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            System.out.println("Waiting for events...");
            // вводим селектор в цикл ожидания ивентов. n - число ключей доступных селектору
            // Это блокирующий вызов
            int n = selector.select();
            System.out.printf("Got %d events\n", n);
            final Set<SelectionKey> keys = selector.selectedKeys();
            for (SelectionKey selectionKey : keys) {
                if ((selectionKey.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                    // block of accepting of a connection
                    System.out.println("Accepting a connection");
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    keys.remove(selectionKey);
                } else if((selectionKey.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                    // block of reading of a content
                    System.out.println("Reading content:\n");
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                    final CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
                    System.out.println(charBuffer.array());
                    charBuffer.clear();
                    keys.remove(selectionKey);
                    selectionKey.cancel();
                    socketChannel.close();
                }
            }
        }

    }
}
