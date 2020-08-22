package nionio2.fs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {

        final List<FileSystemProvider> fileSystemProviders = FileSystemProvider.installedProviders();
        fileSystemProviders.forEach(System.out::println);

        FileSystem fileSystem = FileSystems.getDefault();
        URI rootURI = new URI("file:///");
        FileSystem fileSystem2 = FileSystems.getFileSystem(rootURI);
        System.out.println(fileSystem == fileSystem2);

        // Create directory
//        Path path = Paths.get("files");
//        //OR
//        path = fileSystem1.getPath("files");
//        FileSystemProvider macFSP = fileSystem1.provider();
//        macFSP.createDirectory(path);

        final Iterable<Path> rootDirectories = fileSystem.getRootDirectories();
        rootDirectories.forEach(System.out::println);
        final Iterable<FileStore> fileStores = fileSystem.getFileStores();
        fileStores.forEach(fileStore -> System.out.println(fileStore.name()));
    }
}