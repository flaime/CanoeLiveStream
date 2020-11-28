package liveKanot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fuin.utils4j.Utils4J;

import java.io.File;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Utils4J.addToClasspath("file:///"+System.getProperty("java.home")+ File.separator+"lib"+File.separator+"jfxrt.jar");

        String fxmlFile = "/fxml/sample.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 550, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
