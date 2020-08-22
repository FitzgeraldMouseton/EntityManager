package nionio2.fs;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.Map;

public class JarFSOperations {

    public static void main(String[] args) {

        URI zip = URI.create("jar:file:///Users/scholar/IdeaProjects/pluralsight/lambda-expressions-JosePamaurd/files/archive.zip");
        // помимо create может быть ключ encoding в случае, если надо положить в архив
        // файл с нестандартной кодировкой
        final Map<String, String> options = Map.of("create", "true");
        try (final FileSystem zipFS = FileSystems.newFileSystem(zip, options)) {

            // Пример, когда нужно использовать zipFS.getPath() для получения path, а не стандартный Paths.get(),
            // т.к. Paths.get() вернет path для дефолтной ФС, а нам нужно для jar FS
            Path dir = zipFS.getPath("files/");
            zipFS.provider().createDirectory(dir);
            Path aesop = Path.of("files/aesop.txt");
            Path target = zipFS.getPath("files/aesop-compressed.txt");
            Files.copy(aesop, target);

            // Прямая запись в архив. Например для добавления листа интеджеров сразу в файл в архиве

            Path binDir = zipFS.getPath("bin/");
            Path binFile = zipFS.getPath("bin/ints.bin/");

            zipFS.provider().createDirectory(binDir);
            final OutputStream os = zipFS.provider().newOutputStream(binFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            final DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(10);
            dos.writeInt(20);
            dos.writeInt(30);
            dos.flush();
            dos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
