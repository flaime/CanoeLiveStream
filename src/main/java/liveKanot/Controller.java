package liveKanot;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import liveKanot.entities.Bana;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;


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
        System.out.println("Hej");

        final String loppNummer = this.loppNummer.getText();
        String dbUrl = url.getText() + "/api/competitions/" + competition.getText() + "/" + loppNummer;

        System.out.println("DB url: " + dbUrl);

        final JsonNode races = getRaces(dbUrl);
        System.out.println(races);

        final String raceClass = normaliseRaceClass(races.getObject().getString("raceClass"));
        final String type = normaliseType(races.getObject().getString("type"));
        final String typeNumber = races.getObject().getInt("typeNumber") + "";
        final String distance = races.getObject().getString("distance");

        fileContent.setText("Heat " + loppNummer + " " + raceClass + " " + distance + "m " + type + " " + typeNumber);

        ArrayList<Bana> banor = new ArrayList<>();
        final int racelenght = races.getObject().getJSONArray("tracks").length();
        for (int i = 0; i < racelenght; i++) {
            final JSONObject track = races.getObject().getJSONArray("tracks").getJSONObject(i);
            System.out.println(track);
            int participantSize = track.getJSONArray("persons").length();
            String forname = "";
            for (int y = 0; y < participantSize; y++) {
                forname += track.getJSONArray("persons").getJSONObject(y).getString("forName");
                forname += " " + track.getJSONArray("persons").getJSONObject(y).getString("sureName") + " ";
            }

            if(removeåäö.isSelected())
                forname = replaceåäö(forname);

            final int place = track.getInt("place");
            final String time = track.getString("time");
            final String banNummer = track.getString("trackNumber");

            final Bana bana = new Bana(place+"", forname, time,banNummer);

            banor.add(bana);
        }

        banor.sort(Comparator.comparing(Bana::getBana));
        final boolean timeExist = banor.stream()
                .anyMatch(bana -> !bana.getTid().equals("null"));
        if(timeExist)
            banor.sort(Comparator.comparing(Bana::getTid));

        System.out.println("Time exist" + timeExist);
        String jsonHokus = "{ \"bulkar\" : [ {";


        int bulk = 0;
        for (int i = 0; i < banor.size(); i++) {
            if(bulk ==10){
                bulk = 0;
                jsonHokus += "},{ ";
            }

            Bana bana = banor.get(i);
            jsonHokus += "\"person" + (bulk + 1) + "Name\": \"" + bana.getNamn() + "\",\n";
            jsonHokus += "\"person" + (bulk + 1) + "Bana\": \"" + bana.getBana() + "\",\n";
            jsonHokus += "\"person" + (bulk + 1) + "Placering\": \"" + (timeExist ? (i+1) : "--") + "\",\n";
            jsonHokus += "\"person" + (bulk + 1) + "Time\": \"" + (bana.getTid().equals("null") ? "" : bana.getTid()) + (banor.size() - 1 == i || bulk == 9 ? "\"" : "\",\n");
            bulk++;
        }

        jsonHokus += "}]}";
        json.setText(jsonHokus);

    }

    private String replaceåäö(String forname) {
        forname = forname.replace("å","a");
        forname = forname.replace("Å","A");
        forname = forname.replace("ö","o");
        forname = forname.replace("Ö","O");
        forname = forname.replace("ä","a");
        forname = forname.replace("Ä","A");
        return forname;
    }

    private String normaliseRaceClass(String data) {
        return data.substring(0, 1).equalsIgnoreCase("C") ? data.substring(1, 3) + " C" + data.substring(3, 4) : data.substring(0, 3) + " K" + data.substring(3, 4);
    }

    private String normaliseType(String type) {
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


    public JsonNode getRaces(String url) throws UnirestException {

        HttpResponse<JsonNode> jsonResponse
                = Unirest.get(url)
                .asJson();

        return jsonResponse.getBody();
    }

}
