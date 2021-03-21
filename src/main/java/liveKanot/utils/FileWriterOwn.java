package liveKanot.utils;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriterOwn {


    public static void writeFile(String fileName, String fileContent, String filePath)
            throws IOException {
        String filePathFixed = filePath.trim();
        if (filePathFixed.length() > 0 && !filePathFixed.endsWith("/"))
            filePathFixed = filePathFixed + "/";

        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(filePathFixed + fileName));
        writer.write(fileContent);

        writer.close();
    }
}
