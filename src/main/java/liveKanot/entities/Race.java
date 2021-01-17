package liveKanot.entities;

import com.mashape.unirest.http.JsonNode;
import org.json.JSONObject;

import java.util.ArrayList;

public class Race {
    private ArrayList<Bana> banor;
    private String raceClass;
    private String type;
    private int typeNumber;
    private String distance;
    private String raceNumber;

    public Race(ArrayList<Bana> banor, String raceClass, String type, int typeNumber, String distance, String raceNumber) {
        this.banor = banor;
        this.raceClass = raceClass;
        this.type = type;
        this.typeNumber = typeNumber;
        this.distance = distance;
        this.raceNumber = raceNumber;
    }

    public Race(JsonNode races, String raceNumber, boolean removeÅÄÖ) {
        banor = getBanor(races, removeÅÄÖ);
        raceClass = races.getObject().getString("raceClass");
        type = races.getObject().getString("type");
        typeNumber = races.getObject().getInt("typeNumber");
        distance = races.getObject().getString("distance");
        this.raceNumber = raceNumber;
    }

    public ArrayList<Bana> getBanor() {
        return banor;
    }

    public void setBanor(ArrayList<Bana> banor) {
        this.banor = banor;
    }

    public String getRaceClass() {
        return raceClass;
    }

    public void setRaceClass(String raceClass) {
        this.raceClass = raceClass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(int typeNumber) {
        this.typeNumber = typeNumber;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRaceNumber() {
        return raceNumber;
    }

    public void setRaceNumber(String raceNumber) {
        this.raceNumber = raceNumber;
    }

    private ArrayList<Bana> getBanor(JsonNode races, boolean removeåäö) {
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

            if (removeåäö)
                forname = replaceåäö(forname);

            final int place = track.getInt("place");
            final String time = track.getString("time");
            final String banNummer = track.getString("trackNumber");
            final String clubb = track.getString("clubb");

            final Bana bana = new Bana(place + "", forname, time, banNummer, clubb);

            banor.add(bana);
        }
        return banor;
    }

    private static String replaceåäö(String forname) {
        forname = forname.replace("å", "a");
        forname = forname.replace("Å", "A");
        forname = forname.replace("ö", "o");
        forname = forname.replace("Ö", "O");
        forname = forname.replace("ä", "a");
        forname = forname.replace("Ä", "A");
        return forname;
    }
}
