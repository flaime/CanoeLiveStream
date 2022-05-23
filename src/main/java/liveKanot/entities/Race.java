package liveKanot.entities;

import liveKanot.UiController.SettingsController;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Race {
    private List<Bana> banor;
    private String raceClass;
    private String type;
    private int typeNumber;
    private String distance;
    private String raceNumber;
    private LocalDateTime dateTime;

    public Race(List<Bana> banor, String raceClass, String type, int typeNumber, String distance, String raceNumber) {
        this.banor = banor;
        this.raceClass = raceClass;
        this.type = type;
        this.typeNumber = typeNumber;
        this.distance = distance;
        this.raceNumber = raceNumber;
    }

    public Race(JSONObject races, String raceNumber, boolean removeÅÄÖ) {
        banor = getBanor(races, removeÅÄÖ);
        raceClass = races.getString("raceClass");
        type = races.getString("type");
        typeNumber = races.getInt("typeNumber");
        distance = races.getString("distance");
        dateTime = LocalDateTime.parse(races.getString("dateTime"));
        this.raceNumber = raceNumber;
    }

    public List<Bana> getBanor() {
        return banor;
    }

    public void setBanor(ArrayList<Bana> banor) {
        this.banor = banor;
    }

    public String getRaceClass() {
        return raceClass;
    }

    public String normaliseRaceClass() {
        return raceClass.substring(0, 1).equalsIgnoreCase("C")
                ? raceClass.substring(1, 3) + " C" + raceClass.substring(3, 4)
                : raceClass.substring(0, 3) + " K" + raceClass.substring(3, 4);
    }

    public void setRaceClass(String raceClass) {
        this.raceClass = raceClass;
    }

    public String getType() {
        return type;
    }

    public String normaliseType(SettingsController settings) {
        if (type.equals("FÖ"))
            return settings.getFörsök();
        else if (type.equals("BF"))
            return settings.getBFinal();
        else if (type.equals("AF"))
            return settings.getAFinal();
        else if (type.equals("MH"))
            return settings.getMellanHeat();
        else if (type.length() == 2 && type.substring(1, 2).equals("F"))
            return type.substring(0, 1) + "-" + settings.getFinalÖvrigt();

        else
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    private ArrayList<Bana> getBanor(JSONObject races, boolean removeåäö) {
        ArrayList<Bana> banor = new ArrayList<>();
        final int racelenght = races.getJSONArray("tracks").length();
        for (int i = 0; i < racelenght; i++) {
            final JSONObject track = races.getJSONArray("tracks").getJSONObject(i);
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

    public boolean raceExist() {
        return getRaceNumber() != null;
    }

    public boolean hasResult() {
        if (banor != null) {
            boolean timeExist = banor.stream().anyMatch(bana -> bana.tid != null && !bana.tid.equals("null"));
            return timeExist;
        } else return false;
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
