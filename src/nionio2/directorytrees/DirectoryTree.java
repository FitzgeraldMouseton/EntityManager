package nionio2.directorytrees;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DirectoryTree {

    public static void main(String[] args) throws IOException {

        Path path = Paths.get("files");
        int depth = 3;

        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*");

        DirectoryStream<Path> ds = Files.newDirectoryStream(path, Files::isDirectory);

        List<Path> paths = StreamSupport.stream(ds.spliterator(), false).collect(Collectors.toList());
        ds.forEach(System.out::println);

        ds.close();

        String str = "Fuck you, asshole";
        StringTokenizer tokenizer = new StringTokenizer(str);
        while (tokenizer.hasMoreElements()) {
            System.out.println(tokenizer.nextToken(",").trim());
        }

        final Stream<Path> walk = Files.walk(path, depth, FileVisitOption.FOLLOW_LINKS);
        walk.close();

//        Files.find(path, (path, attributes) ->
    }
}
