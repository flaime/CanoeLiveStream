package liveKanot.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import liveKanot.UiController.SettingsController;
import liveKanot.entities.Race;
import liveKanot.entities.ResultatFileEntity;
import liveKanot.utils.FileWriterOwn;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RaceData {

    public static String getRaceInfoText(Race race, SettingsController settings) {

        final String raceClass = race.normaliseRaceClass();
        final String type = race.normaliseType(settings);

        return "Heat " + race.getRaceNumber() + " " + raceClass + " " + race.getDistance() + "m " + type + " " + race.getTypeNumber();
    }

    public static void createAndWriteResultFiles(Race race, SettingsController settings, int limit, int offset) throws IOException {
        List<ResultatFileEntity> resultFiles = getResultatFileEntitiesSorted(race, settings);

        FileWriterOwn.writeFile(settings.getResultatFile() + ".json",
                RaceData.getResultFilesJson(
                        RaceData.offsetAndLimit(
                                resultFiles,
                                limit,
                                offset
                        )
                ),
                settings.getFilePath());
    }

    private static List<ResultatFileEntity> getResultatFileEntitiesSorted(Race race, SettingsController settings) {
        Comparator<ResultatFileEntity> compareByBana = Comparator
                .comparing(ResultatFileEntity::getLoppTid)
                .thenComparing(ResultatFileEntity::getBana);
        List<ResultatFileEntity> resultFiles = RaceData.getResultatFileEntityForRace(race, settings).stream().sorted(compareByBana).collect(Collectors.toList());
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