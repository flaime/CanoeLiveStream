package liveKanot.UiController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.application.Platform;
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
import liveKanot.data.ProgramData;
import liveKanot.data.RaceData;
import liveKanot.utils.*;
import liveKanot.data.DataFetcher;
import liveKanot.entities.Race;

import java.io.IOException;
import java.util.*;

import static spark.Spark.*;
import static spark.Spark.get;

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

    @FXML
    private ToggleButton autoadvance;

    SaveController saveController = null;
    private Stage stage;

    public void setStageAndSetupListeners(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                saveController.safeSave();
                Platform.exit();
                System.exit(0);
            }
        });
    }

    @FXML
    protected void initialize() throws IOException {
        List<Control> toSave = new LinkedList<>(Arrays.asList(
                loppNummerStartListor,
                loppNummerResultatListor,
                importantMessage,
                message
        ));

        loppNummerResultatListor.setTextFormatter(ControllerUtils.getNumberFormaterForTextField());
        loppNummerStartListor.setTextFormatter(ControllerUtils.getNumberFormaterForTextField());

        toSave.addAll(createSettingsDialog());

        saveController = new SaveController(toSave);
        saveController.loadSettings();
        if (settings.getCompetition().isEmpty() || settings.getUrl().isEmpty()) {
            promtForUrlAndCompetitionname();
            saveController.safeSave();
        }
        createAndUpdateProgramFile();

        startAPI(settings);
        startAutoAdvance();
    }

    public static boolean runAutoadvanced = false;

    private void startAutoAdvance() {
        MainController mainController = this;
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    if (runAutoadvanced) {
                        int presentResultNumber = Integer.valueOf(loppNummerResultatListor.getText());
                        int nextRace = ControllerUtils.findNextRace(presentResultNumber, mainController);

                        loppNummerResultatListor.setText(nextRace + "");
                        updateResults();
                    }
                } catch (NumberFormatException | IOException e) {
                    System.out.println("Could not load current race number for autoadvance");
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 5000L, settings.getaAutoadvanceTime() * 1000L);

        autoadvance.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("Start");
                runAutoadvanced = true;
            } else {
                runAutoadvanced = false;
                System.out.println("stop");
            }
        }));

    }

    public void startAPI(SettingsController settings) {
        port(settings.getApiPort());
        get("/resultatListor", (req, res) -> loppNummerResultatListor.getText());
        //Below is a bad use of get but to mor easy of use int the browser so this will make an update
        get("/resultatListor/update/:number", (req, res) -> {
            loppNummerResultatListor.setText(req.params("number"));
            updateResults();
            return req.params("number");
        });

        get("/resultatListor/next", (req, res) -> {
            nextRaceResults();
            return loppNummerResultatListor.getText();
        });

        get("/resultatListor/previous", (req, res) -> {
            previousRaceResults();
            return loppNummerResultatListor.getText();
        });


        //--------------------------------------//

        get("/startListor", (req, res) -> loppNummerStartListor.getText());
        //Below is a bad use of get but to mor easy of use int the browser so this will make an update
        get("/startListor/update/:number", (req, res) -> {
            loppNummerStartListor.setText(req.params("number"));
            updateStartListor();
            return req.params("number");
        });

        get("/startListor/next", (req, res) -> {
            nextRaceStartList();
            nextRace(loppNummerStartListor);
            return loppNummerStartListor.getText();
        });

        get("/startListor/previous", (req, res) -> {
            previousRaceStartList();
            return loppNummerStartListor.getText();
        });
    }

    public void createAndUpdateProgramFile() {
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    System.out.println("uppdating program file");
                    ProgramData.createAndWriteProgramFile(new DataFetcher().getRaces(settings.getCompetition(), settings.getUrl(), settings.removeåäö()), settings);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 5000L, settings.getProgramFileUpdateTime() * 1000L);
    }

    public void promtForUrlAndCompetitionname() {
        Stage newStage = new Stage();

        VBox comp = new VBox();
        comp.setSpacing(10);
        comp.setPadding(new Insets(10, 10, 10, 10));

        Label info = new Label("Minst en av dessa är inte satta, se till att fylla i båda.");
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
        FileWriterOwn.writeFile(settings.getImportantMessageFile() + ".txt", importantMessage.getText(), settings.getFilePath());
    }

    @FXML
    public void saveMessage() throws IOException {
        FileWriterOwn.writeFile(settings.getMessageFile() + ".txt", message.getText(), settings.getFilePath());
    }

    @FXML
    public void nextRaceResults() throws IOException {
        nextRace(loppNummerResultatListor);
        updateResults();
    }

    @FXML
    public void nextRaceStartList() throws IOException {
        nextRace(loppNummerStartListor);
        updateStartListor();
        callNextChapter();
    }

    private void callNextChapter() {
        try {
            int raceNumber = Integer.parseInt(loppNummerStartListor.getText());
            Race race = getData(loppNummerStartListor.getText());
            String info = RaceData.getRaceInfoText(race, settings);
            new DataFetcher().puchChapterData(settings, raceNumber, info, race.normaliseRaceClass(), race.getDistance());
        } catch (NumberFormatException e) {
            loppNummerStartListor.setText("");
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void previousRaceResults() throws IOException {
        previousRace(loppNummerResultatListor);
        updateResults();
    }

    @FXML
    public void previousRaceStartList() throws IOException {
        previousRace(loppNummerStartListor);
        updateStartListor();
        callNextChapter();
    }

    private void previousRace(TextField textField) {
        try {
            Integer result = Integer.valueOf(textField.getText());

            textField.setText((result - 1) + "");

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
    public void updateResults() throws IOException {
        Race race = getData(loppNummerResultatListor.getText());

        resultHeder.setText(RaceData.getRaceInfoText(race, settings));

        RaceData.createAndWriteResultFiles(race, settings, settings.getResultatFile(), 15, 0);
    }

    @FXML
    public void updateStartListor() throws IOException {
        Race race = getData(loppNummerStartListor.getText());

        startlistHeder.setText(RaceData.getRaceInfoText(race, settings));

        TimeStampUtils.addEvent(race.getRaceNumber(), settings);
        RaceData.createAndWritProgramFiles(race, settings, settings.getStartlistaFile(), 15, 0);
    }

    private Stage settingsStage = new Stage();
    SettingsController settings = new SettingsController();

    @FXML
    public void showVersionLogInfo() {

        List<String> log = Arrays.asList(
                "1.5.0 - Uppdatera sorteringen av programfilen",
                "1.6.0 - Sorterar banorna korrekt efter bannummer i form av tal och lagt till detta fönster"
        );
        Collections.reverse(log);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Versionslogg");
        alert.setHeaderText("Vad som har uppdaterats med var version");
        alert.setContentText(String.join("\n", log));

        alert.showAndWait();

    }

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
