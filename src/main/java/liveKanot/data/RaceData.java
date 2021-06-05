package liveKanot.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import liveKanot.UiController.SettingsController;
import liveKanot.entities.Race;
import liveKanot.entities.ResultatFileEntity;
import liveKanot.utils.FileWriterOwn;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RaceData {

    public static String getRaceInfoText(Race race, SettingsController settings) {

        final String raceClass = race.normaliseRaceClass();
        final String type = race.normaliseType(settings);

        return "Heat " + race.getRaceNumber() + " " + raceClass + " " + race.getDistance() + "m " + type + " " + race.getTypeNumber();
    }

    public static void createAndWritProgramFiles(Race race, SettingsController settings, String fileName, int limit, int offset) throws IOException {
        List<ResultatFileEntity> resultFiles = sortedForProgram(race, RaceData.getResultatFileEntityForRace(race, settings), settings);
        addExtra(resultFiles, 20);
        createAndWriteFile(race, settings, fileName, resultFiles, limit, offset);
    }

    public static void createAndWriteResultFiles(Race race, SettingsController settings, String fileName, int limit, int offset) throws IOException {
        List<ResultatFileEntity> resultFiles = sortedForResult(race, RaceData.getResultatFileEntityForRace(race, settings), settings);
        addExtra(resultFiles, 20);

        createAndWriteFile(race, settings, fileName, resultFiles, limit, offset);
    }

    private static void addExtra(List<ResultatFileEntity> resultFiles, int numberToAdd) {
        IntStream.range(0, numberToAdd).forEach((it) -> {
            ResultatFileEntity resultatFileEntity = new ResultatFileEntity("",
                    resultFiles.isEmpty() ? LocalDateTime.now() : resultFiles.get(0).getStartTid(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "");
            resultFiles.add(resultatFileEntity);
        });
    }

    public static void createAndWriteFile(Race race, SettingsController settings, String fileName, List<ResultatFileEntity> resultFiles, int limit, int offset) throws IOException {
        FileWriterOwn.writeFile(fileName + ".json",
                RaceData.getResultFilesJson(
                        RaceData.offsetAndLimit(
                                resultFiles,
                                limit,
                                offset
                        )
                ),
                settings.getFilePath());
    }

    private static List<ResultatFileEntity> sortedForResult(Race race, List<ResultatFileEntity> resultatAndProgramFileEntityForRace, SettingsController settings) {
        Comparator<ResultatFileEntity> compareByBana = Comparator
                .comparing(ResultatFileEntity::getLoppTid)
                .thenComparing(ResultatFileEntity::getBana);
        List<ResultatFileEntity> resultFiles = resultatAndProgramFileEntityForRace.stream().sorted(compareByBana).collect(Collectors.toList());
        return resultFiles;
    }

    private static List<ResultatFileEntity> sortedForProgram(Race race, List<ResultatFileEntity> resultatAndProgramFileEntityForRace, SettingsController settings) {
        Comparator<ResultatFileEntity> compareByBana = Comparator
                .comparing(ResultatFileEntity::getBana);
        List<ResultatFileEntity> resultFiles = resultatAndProgramFileEntityForRace.stream().sorted(compareByBana).collect(Collectors.toList());
        return resultFiles;
    }

    public static List<ResultatFileEntity> offsetAndLimit(List<ResultatFileEntity> resultatFileEntities, int limit, int offset) throws JsonProcessingException {
        return resultatFileEntities.stream()
                .skip(offset)  // The equivalent to SQL's offset
                .limit(limit) // The equivalent to SQL's limit
                .collect(Collectors.toList());
    }

    public static String getResultFilesJson(List<ResultatFileEntity> resultatFileEntities) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(resultatFileEntities);
    }

    public static List<ResultatFileEntity> getResultatFileEntityForRace(Race race, SettingsController settings) {
        return race.getBanor().stream().map(bana ->
                new ResultatFileEntity(
                        race.getRaceNumber(),
                        race.getDateTime(),
                        race.normaliseRaceClass(),
                        (race.getDistance() + "m"),
                        race.normaliseType(settings),
                        bana.getBana(),
                        bana.getNamn(),
                        bana.getClubb(),
                        (race.normaliseRaceClass() + " " + race.getDistance() + "m " + race.normaliseType(settings) + " " + race.getTypeNumber()),
                        bana.getTid(),
                        bana.getPlacering()
                )
        ).collect(Collectors.toList());
    }
}