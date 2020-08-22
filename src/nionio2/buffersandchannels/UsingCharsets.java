package nionio2.buffersandchannels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class UsingCharsets {
    public static void main(String[] args) throws IOException {

        Charset latin1 = StandardCharsets.ISO_8859_1;
        Charset utf = StandardCharsets.UTF_8;

        String hello = "Helló";

//        System.out.println("Length: " + hello.length());

        CharBuffer charBuffer = CharBuffer.allocate(1024 * 1024);
        charBuffer.put(hello);
        charBuffer.flip();

        System.out.println(charBuffer.position());
        System.out.println(charBuffer.limit());

        Path path = Path.of("data/hello.txt");
        if (!Files.exists(path)) {
            Files.createFile(path);
        }

        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            fileChannel.write(utf.encode(charBuffer));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("File size: " + Files.size(path));

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            charBuffer.clear();
            fileChannel.read(byteBuffer);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        byteBuffer.flip();

        String string = new String(utf.decode(byteBuffer).array());
        System.out.println(string);
    }
}
