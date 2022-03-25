package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;

public class Futil {
    public static void processDir(String dirName, String resultFileName){
        List<String> list = new ArrayList<>();

        SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                List<String> lines = Files.readAllLines(file, Charset.forName("Cp1250"));
                list.addAll(lines);
                return FileVisitResult.CONTINUE;
            }
        };

        try {
            Files.walkFileTree(Paths.get(dirName), visitor);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path resultPath = Paths.get(resultFileName);
        String TextInFiles = String.join("\n", list);

        try (FileChannel fileChannel = FileChannel.open(resultPath, CREATE, TRUNCATE_EXISTING, WRITE)){
            fileChannel.write(ByteBuffer.wrap(TextInFiles.getBytes(StandardCharsets.UTF_8)));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
