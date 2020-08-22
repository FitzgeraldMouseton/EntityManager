package nionio2.directorytrees;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.*;
import java.util.stream.Stream;

public class FindingFiles {

    public static void main(String[] args) throws IOException {

        Path path = Paths.get("/Volumes/Mojave/Users/scholar/Downloads");
        System.out.println(Files.exists(path));

        // Подсчет количества файлов
        final Stream<Path> allFiles = Files.find(path, Integer.MAX_VALUE, (p, attr) -> true);
        System.out.println("count: " + allFiles.count());

        final Stream<Path> allJPGs = Files.find(path, Integer.MAX_VALUE, (p, attr) -> p.toString().endsWith(".jpg"));
        System.out.println("count: " + allJPGs.count());

//        BasicFileAttributes attributes = null;
//        final FileTime creationTime = attributes.creationTime();
//        creationTime.toMillis();
        LocalDateTime date = LocalDateTime.of(2019, 12, 1, 0, 0);
        long dateMillis = date.toInstant(ZoneOffset.UTC).toEpochMilli();
        final Stream<Path> allBefore = Files.find(path, Integer.MAX_VALUE, (p, attr) -> attr.creationTime().toMillis() < dateMillis);
        System.out.println("count: " + allBefore.count());

    }
}
