package liveKanot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import liveKanot.UiController.MainController;
import org.fuin.utils4j.Utils4J;

import java.io.File;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Utils4J.addToClasspath("file:///"+System.getProperty("java.home")+ File.separator+"lib"+File.separator+"jfxrt.jar");

        String fxmlFile = "/fxml/GUI.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        MainController controller = (MainController)loader.getController();
        controller.setStageAndSetupListeners(primaryStage);
        primaryStage.setTitle("Live stream");
        primaryStage.setScene(new Scene(root, 605, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
