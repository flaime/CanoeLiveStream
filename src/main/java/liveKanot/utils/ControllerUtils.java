package liveKanot.utils;

import javafx.scene.control.TextFormatter;
import liveKanot.UiController.MainController;
import liveKanot.entities.Race;
import org.json.JSONException;

import java.util.Arrays;
import java.util.Random;
import java.util.function.UnaryOperator;

public class ControllerUtils {

    public static TextFormatter<Object> getNumberFormaterForTextField() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        return new TextFormatter<>(filter);
    }


    public static int findNextRace(int currentRace, MainController mainController) {
        System.out.println("Autoadvance updated");
        int nextResultNumber = ++currentRace;
        for (int i = 0; i < 30; i++) {
            Race race = mainController.getData(nextResultNumber + "");
            if (race.raceExist() && race.hasResult())
                return nextResultNumber;
            nextResultNumber++;
        }
        return 1;

    }

    /**
     * Generates an random API key as example
     * oFhWNPqRxPwUoEMMBiUijNpDJxiaIlAXtTiimBWvFnfHl
     *
     * @return random API key
     */
    public static String generateApiKey() {
        //ascii-table
        int leftLimit = 65; // letter 'A'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 45;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(value -> !Arrays.asList(91, 92, 93, 94, 95, 96).contains(value))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        System.out.println(generatedString);
        return generatedString;
    }
}
