package liveKanot.utils;

import javafx.scene.control.TextFormatter;

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
}
