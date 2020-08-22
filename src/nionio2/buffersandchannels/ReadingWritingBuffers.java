package nionio2.buffersandchannels;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ReadingWritingBuffers {
    public static void main(String[] args) throws IOException {

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
        byteBuffer.putInt(10);
        byteBuffer.putInt(20);
        byteBuffer.putInt(30);

        final Path path = Path.of("data/int.bin");
        final Path directoryPath = Path.of("data");

        try(FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            byteBuffer.flip();
            fileChannel.write(byteBuffer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File size: " + Files.size(path));

        try(FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            byteBuffer.clear();
            fileChannel.read(byteBuffer);
            System.out.println("Position: " + byteBuffer.position());
            System.out.println("Limit: " + byteBuffer.limit());

        } catch (IOException e) {
            e.printStackTrace();
        }

        byteBuffer.flip();

        System.out.println("Position: " + byteBuffer.position());
        System.out.println("Limit: " + byteBuffer.limit());

        IntBuffer intBuffer = byteBuffer.asIntBuffer();

        try {
            while (true) {
                int i = intBuffer.get();
                System.out.println("i: " + i);
            }
        } catch (BufferUnderflowException ex) {
        }

        System.out.println("File size: " + Files.size(path));
    }
}
