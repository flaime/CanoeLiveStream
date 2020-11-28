package liveKanot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReaderOwn {


    public static String readFile(String fileName)
            throws IOException {
        return  new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
