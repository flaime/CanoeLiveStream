package liveKanot;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriterOwn {


    public static void writeFile(String fileName, String fileContent)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(fileName));
        writer.write(fileContent);

        writer.close();
    }
}
