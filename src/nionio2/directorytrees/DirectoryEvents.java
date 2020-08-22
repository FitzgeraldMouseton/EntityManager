package nionio2.directorytrees;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class DirectoryEvents {
    public static void main(String[] args) throws IOException, InterruptedException {

        Path dir = Path.of("/Volumes/Mojave/Users/scholar/Downloads/Events");
        FileSystem fileSystem = dir.getFileSystem();

        WatchService watchService = fileSystem.newWatchService();
        final WatchKey key = dir.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);

        while (key.isValid()) {
            final WatchKey take = watchService.take();
            final List<WatchEvent<?>> watchEvents = take.pollEvents();
            for (WatchEvent<?> event: watchEvents) {
                if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                } else if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    Path path = (Path) event.context();
                    System.out.println("File created: " + path + " - " + Files.probeContentType(path));
                } else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    Path path = (Path) event.context();
                    System.out.println("File modified: " + path + " - " + Files.probeContentType(path));
                } else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    Path path = (Path) event.context();
                    System.out.println("File deleted: " + path + " - " + Files.probeContentType(path));
                }
                take.reset();
            }
        }
        System.out.println("key is invalid");

    }
}
