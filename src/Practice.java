import java.io.IOException;
import java.net.URI;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Practice {

    public static void main(String[] args) throws IOException {

        String initialPath = "file:///Volumes/Mojave/Users/scholar/Documents/Practice/1/2/3";
        Path path = Path.of(URI.create(initialPath));

        Files.createDirectories(path);
        FileSystem fileSystem = path.getFileSystem();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.putInt(10);
        byteBuffer.putInt(20);
        byteBuffer.putInt(30);

        path = Paths.get(initialPath + "/ints.bin");
        Path stringPath = Paths.get(initialPath + "/string.txt");
//        Files.createDirectories(path);
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);){
            byteBuffer.flip();
            fileChannel.write(byteBuffer);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("size: " + Files.size(path));

        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);){
            byteBuffer.clear();
            fileChannel.read(byteBuffer);
            byteBuffer.flip();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        try {
            while (true) {
                int i = intBuffer.get();
                System.out.println("i: " + i);
            }
        } catch (BufferUnderflowException ex) {
        }

        Charset utf = StandardCharsets.UTF_8;
        String hello = "hello";
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put(hello);
        charBuffer.put(hello);

        try (FileChannel fileChannel = FileChannel.open(stringPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);){
            byteBuffer.clear();
            charBuffer.flip();
            fileChannel.write(utf.encode(charBuffer));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (FileChannel fileChannel = FileChannel.open(stringPath, StandardOpenOption.READ);){
            byteBuffer.clear();
            charBuffer.clear();
            fileChannel.read(byteBuffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        charBuffer = utf.decode(byteBuffer.flip());
        System.out.println(charBuffer.array());

        try (FileChannel fileChannel = FileChannel.open(stringPath, StandardOpenOption.APPEND)) {
            charBuffer.clear();
            charBuffer.put("hello");
            fileChannel.write(utf.encode(charBuffer));
        }
    }
}
