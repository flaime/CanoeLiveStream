package liveKanot.UiController;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

public class SettingsController {

    @FXML
    private TextField url;

    @FXML
    private TextField competition;

    @FXML
    private TextField titleFile;

    @FXML
    private TextField lopFile;

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

    private MainController mainController;
    public void setMainUi(MainController controller) {
        mainController = controller;
    }

    public List<Control> toSave() {
        return Arrays.asList(
                url,
                competition,
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
    }

    public String getCompetition() {
        return competition.getText();
    }

    public String getUrl() {
        return url.getText();
    }

    public boolean removeåäö() {
        return removeåäö.isSelected();
    }

    public String getFörsök() {
        return FÖ.getText();
    }

    public String getBFinal() {
        return BF.getText();
    }

    public String getAFinal() {
        return BF.getText();
    }

    public String getMellanHeat() {
        return MH.getText();
    }

    public String getFinalÖvrigt() {
        return finalÖvrig.getText();
    }

    public String getTitleFile() {
        return titleFile.getText();
    }

    public String getLoppFile() {
        return lopFile.getText();
    }

    public void setCompetition(String competitionName) {
        competition.setText(competitionName);
    }

    public void setUrl(String url) {
        this.url.setText(url);
    }
}
