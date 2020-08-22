package nionio2.directorytrees;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.stream.StreamSupport;

public class VisitingDirectories {

    public static void main(String[] args) throws IOException {

        Path path = Path.of("/Volumes/Mojave/Users/scholar/Downloads");

        // count number of empty directories
        // count number of files/type

        CustomFileVisitor customFileVisitor = new CustomFileVisitor();
        Files.walkFileTree(path, customFileVisitor);
        final long emptyDirsCount = customFileVisitor.getEmptyDirsCount();
        final HashMap<String, Long> fileTypes = customFileVisitor.getFileTypes();
        System.out.println("empty: " + emptyDirsCount);
        fileTypes.forEach((k, v) -> System.out.println(k + ": " + v));

    }

    static class CustomFileVisitor implements FileVisitor<Path> {

        private long emptyDirsCount = 0L;
        private HashMap<String, Long> fileTypes = new HashMap<String, Long>();

        public long getEmptyDirsCount() {
            return emptyDirsCount;
        }

        public HashMap<String, Long> getFileTypes() {
            return fileTypes;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir);
            final boolean present =
                    StreamSupport.stream(directoryStream.spliterator(), false).findFirst().isPresent();// false - not parallel stream
            if (present) {
                emptyDirsCount++;
                return FileVisitResult.CONTINUE;
            } else {
                return FileVisitResult.SKIP_SUBTREE;
            }
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            final String filetype = Files.probeContentType(file);
            fileTypes.merge(filetype, 1L, (v1, v2) -> v1 + v2);

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }
}
