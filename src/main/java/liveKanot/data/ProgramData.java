package liveKanot.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import liveKanot.UiController.SettingsController;
import liveKanot.entities.ProgramFileEntity;
import liveKanot.entities.Race;
import liveKanot.utils.FileWriterOwn;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ProgramData {

    public static void createAndWriteProgramFile(List<Race> races, SettingsController settings) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        FileWriterOwn.writeFile(settings.getProgramFile() + ".json",
                objectMapper.writeValueAsString(Arrays.asList(getProgramObject(races, settings))),
                settings.getFilePath());
    }

    public static ProgramFileEntity getProgramObject(List<Race> races, SettingsController settings) {
        ProgramFileEntity programFileEntity = new ProgramFileEntity("", "", "", "", "");
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma");

        races.stream().forEach(it -> {
            programFileEntity.setHeat(programFileEntity.getHeat() + (programFileEntity.getHeat().equals("") ? "" : "\n") + it.getRaceNumber());
            programFileEntity.setDag_Tid(programFileEntity.getDag_Tid() + (programFileEntity.getDag_Tid().equals("") ? "" : "\n") + it.getDateTime().format(customFormatter));
            programFileEntity.setKlass(programFileEntity.getKlass() + (programFileEntity.getKlass().equals("") ? "" : "\n") + it.normaliseRaceClass());
            programFileEntity.setDistans(programFileEntity.getDistans() + (programFileEntity.getDistans().equals("") ? "" : "\n") + it.getDistance() + " m");
            programFileEntity.setTyp(programFileEntity.getTyp() + (programFileEntity.getTyp().equals("") ? "" : "\n") + it.normaliseType(settings) + " " + it.getTypeNumber());
        });

        return programFileEntity;
    }

}
