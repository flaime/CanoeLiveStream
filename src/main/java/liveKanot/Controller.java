package liveKanot;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import liveKanot.data.DataFetcher;
import liveKanot.data.RaceData;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Controller {


    @FXML
    private TextField url;

    @FXML
    private TextField competition;

    @FXML
    private TextField loppNummer;

    @FXML
    private TextField fileContent;

    @FXML
    private TextField titleFile;

    @FXML
    private TextField lopFile;

    @FXML
    private TextArea json;

    @FXML
    private CheckBox removeåäö;

    //steetings input----
    @FXML
    private TextField MH;
    @FXML
    private TextField AF;
    @FXML
    private TextField BF;
    @FXML
    private TextField FÖ;
    @FXML
    private TextField finalÖvrig;
    @FXML
    private TextField meter;
    @FXML
    private TextField heat;

    //-----

    SaveController saveController = null;

    @FXML
    protected void initialize() throws IOException {
        List<Control> toSave = Arrays.asList(
                url,
                competition,
                loppNummer,
                titleFile,
                lopFile,
                removeåäö,
                MH,
                AF,
                BF,
                FÖ,
                finalÖvrig,
                meter,
                heat
        );

        saveController = new SaveController(toSave);
        saveController.loadSettings();
    }

    @FXML
    public void nextRaceAndUpdate() throws IOException {
        nextRace();
        writeToFile();
    }

    @FXML
    public void nextRace() {
        try {
            Integer result = Integer.valueOf(loppNummer.getText());

            loppNummer.setText((result + 1) + "");

            updateData();
        } catch (Exception e) {
            loppNummer.setText("");
        }

    }

    @FXML
    public void previousRaceAndUpdate() throws IOException {
        previousRace();
        writeToFile();
    }

    @FXML
    public void previousRace() {
        try {
            Integer result = Integer.valueOf(loppNummer.getText());

            loppNummer.setText((result - 1) + "");

            updateData();
        } catch (Exception e) {
            loppNummer.setText("");
        }
    }

    @FXML
    public void writeToFile() throws IOException {
        FileWriterOwn.writeFile(titleFile.getText() + ".txt", fileContent.getText());
        FileWriterOwn.writeFile(lopFile.getText() + ".json", json.getText());
    }

    @FXML
    public void updateData() throws UnirestException {
        System.out.println("Start update");

        final String raceNumber = this.loppNummer.getText();

        final JsonNode races = new DataFetcher().getRace(raceNumber, url.getText(), competition.getText());

        fileContent.setText(RaceData.getHeaderText(races, raceNumber, this));

        json.setText(RaceData.getRacesJson(races, removeåäö.isSelected()));

    }

    public String normaliseType(String type) {
        if (type.equals("FÖ"))
            return FÖ.getText();
        else if (type.equals("BF"))
            return BF.getText();
        else if (type.equals("AF"))
            return AF.getText();
        else if (type.equals("MH"))
            return MH.getText();
        else if (type.length() == 2 && type.substring(1, 1).equals("F"))
            return type.substring(0, 1) + finalÖvrig.getText();

        else
            return type;
    }
}
