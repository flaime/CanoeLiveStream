package liveKanot.UiController;

import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import liveKanot.utils.FileWriterOwn;
import liveKanot.utils.OpenUrlInBrowser;
import liveKanot.utils.SaveController;
import liveKanot.data.DataFetcher;
import liveKanot.entities.Race;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainController {

    @FXML
    private TextField loppNummerStartListor;

    @FXML
    private TextField loppNummerResultatListor;

    @FXML
    private TextArea resultHeder;

    @FXML
    private TextArea startlistHeder;

    @FXML
    private TextField importantMessage;

    @FXML
    private TextField message;

    SaveController saveController = null;
    private Stage stage;

    public void setStageAndSetupListeners(Stage stage) {
        stage = stage;
    }

    @FXML
    protected void initialize() throws IOException {
        List<Control> toSave = new LinkedList<>(Arrays.asList(
                loppNummerStartListor,
                loppNummerResultatListor,
                importantMessage,
                message
        ));

        toSave.addAll(createSettingsDialog());

        saveController = new SaveController(toSave);
        saveController.loadSettings();
        if (settings.getCompetition().isEmpty() || settings.getUrl().isEmpty()) {
            promtForUrlAndCompetitionname();
            saveController.safeSave();
        }
    }

    public void promtForUrlAndCompetitionname() {
        Stage newStage = new Stage();

        VBox comp = new VBox();
        comp.setSpacing(10);
        comp.setPadding(new Insets(10, 10, 10, 10));

        Label info = new Label("En av dessa är inte satta, se till att fylla i båda.");
        comp.getChildren().add(info);

        HBox competition = new HBox();
        Label competitionHelp = new Label("Tävlingens namn:  ");
        TextField competitionName = new TextField(settings.getCompetition());
        competition.getChildren().add(competitionHelp);
        competition.getChildren().add(competitionName);

        HBox url = new HBox();
        TextField urlText = new TextField(settings.getUrl());
        Label urlHelp = new Label("URL:  ");
        url.getChildren().add(urlHelp);
        url.getChildren().add(urlText);

        Button ok = new Button("Spara");
        ok.setOnAction(action -> {
            if (competitionName.getText().isEmpty() || urlText.getText().isEmpty()) {
                info.setText("Du måste skriva in värden för båda dessa för du kan gå vidare.");
            } else {
                settings.setCompetition(competitionName.getText());
                settings.setUrl(urlText.getText());
                newStage.close();
            }
        });

        newStage.setOnCloseRequest(event -> {
            if (competitionName.getText().isEmpty() || urlText.getText().isEmpty()) {
                event.consume();
                info.setText("Du måste skriva in värden för båda dessa för du kan gå vidare.");
            } else {
                settings.setCompetition(competitionName.getText());
                settings.setUrl(urlText.getText());
                newStage.close();
            }
        });

        comp.getChildren().add(competition);
        comp.getChildren().add(url);
        comp.getChildren().add(ok);

        Scene stageScene = new Scene(comp, 350, 150);
        newStage.setScene(stageScene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();
    }

    @FXML
    public void close() {
        saveController.safeSave();
        System.exit(0);
    }

    @FXML
    public void openCompetitionProgram() {
        OpenUrlInBrowser.openURL(settings.getUrl() + "/competition/" + settings.getCompetition());
    }

    @FXML
    public void saveImportantMessage() throws IOException {
        FileWriterOwn.writeFile(settings.getImportantMessageFile() + ".txt", importantMessage.getText());
    }

    @FXML
    public void saveMessage() throws IOException {
        FileWriterOwn.writeFile(settings.getMessageFile() + ".txt", message.getText());
    }

    @FXML
    public void nextRaceResultAndUpdate() throws IOException {
        nextRaceResults();
        writeToFile();
    }

    @FXML
    public void nextRaceResults() {
        nextRace(loppNummerResultatListor);
        updateResults();
    }

    @FXML
    public void nextRaceStartList() {
        nextRace(loppNummerStartListor);
        updateStartListor();
    }

    @FXML
    public void previousRaceResults() {
        previousRace(loppNummerResultatListor);
        updateResults();
    }

    @FXML
    public void previousRaceStartList() {
        previousRace(loppNummerStartListor);
        updateStartListor();
    }

    private void previousRace(TextField textField) {
        try {
            Integer result = Integer.valueOf(textField.getText());

            textField.setText((result - 1) + "");

//            updateData();
        } catch (Exception e) {
            textField.setText("");
        }
    }

    public void nextRace(TextField textField) {
        try {
            Integer result = Integer.valueOf(textField.getText());

            textField.setText((result + 1) + "");

//            updateData();
        } catch (Exception e) {
            textField.setText("");
        }

    }

    @FXML
    public void updateResults() {
        Race race = getData(loppNummerResultatListor.getText());

        resultHeder.setText(race.getDistance() + " m " + race.normaliseType(settings) + " " + race.normaliseRaceClass());

//        fileContent.setText(RaceData.getHeaderText(race, this));//TODO need to change to corect file
//
//        json.setText(RaceData.racesJson(race, 15, 0));//TODO need to change to corect file
    }

    @FXML
    public void updateStartListor() {
        Race race = getData(loppNummerStartListor.getText());

//        fileContent.setText(RaceData.getHeaderText(race, this)); //TODO need to change to corect file
//
//        json.setText(RaceData.racesJson(race, 15, 0));//TODO need to change to corect file
    }

    @FXML
    public void previousRaceAndUpdate() throws IOException {
        previousRaceResults();
        writeToFile();
    }

    @FXML
    public void writeToFile() throws IOException {
//        FileWriterOwn.writeFile(settings.getTitleFile() + ".txt", fileContent.getText());
//        FileWriterOwn.writeFile(settings.getLoppFile() + ".json", json.getText());
    }

    private Stage settingsStage = new Stage();
    SettingsController settings = new SettingsController();

    @FXML
    public void openSettings() throws IOException {
        settingsStage.show();
    }

    private List<Control> createSettingsDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainController.class.getResource("/fxml/settings.fxml"));
        VBox page = loader.load();
        Scene scene = new Scene(page);

        settingsStage.setTitle("Inställningar");
        settingsStage.setScene(scene);

        scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                event.consume();
                settingsStage.hide();
            }
        });

        settings = loader.getController();
        settings.setMainUi(this);
        return settings.toSave();
    }

    public Race getData(String raceNumber) {
        System.out.println("Start fetch");

        try {
            return new DataFetcher().getRace(raceNumber, settings.getCompetition(), settings.getUrl(), settings.removeåäö());
        } catch (UnirestException e) {
            e.printStackTrace();
            //TODO add error handeling
            loppNummerResultatListor.setText("Could not write to file");
            loppNummerStartListor.setText("Could not write to file");
        }
        return new Race(null, null, true);
    }

}
