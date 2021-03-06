package liveKanot.utils;

import javafx.scene.control.TextFormatter;
import liveKanot.UiController.MainController;
import liveKanot.entities.Race;
import org.json.JSONException;

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
        for (int i = 0; i < 20; i++) {
            try {
                Race race = mainController.getData(nextResultNumber + "");
                System.out.println(race);
                return nextResultNumber;
            } catch (JSONException e) {
                System.out.println("Race does not exist");
            }
        }
        return 1;

    }
}
