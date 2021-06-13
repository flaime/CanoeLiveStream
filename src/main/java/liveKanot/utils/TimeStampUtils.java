package liveKanot.utils;

import liveKanot.UiController.SettingsController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampUtils {

    public static void addEvent(String raceNumber, SettingsController settings) throws IOException {

        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String timeNow = LocalDateTime.now().format(customFormatter);

        FileWriterOwn.appendRow(timeNow + " race: " + raceNumber, settings.getTimestampFil() + ".txt", settings.getFilePath());
    }

}
