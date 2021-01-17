package liveKanot.data;

import com.mashape.unirest.http.JsonNode;
import liveKanot.Controller;
import liveKanot.entities.Bana;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class RaceData {

    public static String getHeaderText(JsonNode races, String raceNumber,Controller backReference){

        final String raceClass = normaliseRaceClass(races.getObject().getString("raceClass"));
        final String type = backReference.normaliseType(races.getObject().getString("type"));
        final String typeNumber = races.getObject().getInt("typeNumber") + "";
        final String distance = races.getObject().getString("distance");

        return "Heat " + raceNumber + " " + raceClass + " " + distance + "m " + type + " " + typeNumber;
    }

    public static String getRacesJson(JsonNode races,boolean removeåäö) {
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

            if(removeåäö)
                forname = replaceåäö(forname);

            final int place = track.getInt("place");
            final String time = track.getString("time");
            final String banNummer = track.getString("trackNumber");
            final String clubb = track.getString("clubb");

            final Bana bana = new Bana(place+"", forname, time,banNummer,clubb);

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
            jsonHokus += "\"person" + (bulk + 1) + "Clubb\": \"" + bana.getClubb() + "\",\n";
            bulk++;
        }

        jsonHokus += "}]}";
        return jsonHokus;
    }

    private static String normaliseRaceClass(String data) {
        return data.substring(0, 1).equalsIgnoreCase("C") ? data.substring(1, 3) + " C" + data.substring(3, 4) : data.substring(0, 3) + " K" + data.substring(3, 4);
    }

    private static String replaceåäö(String forname) {
        forname = forname.replace("å","a");
        forname = forname.replace("Å","A");
        forname = forname.replace("ö","o");
        forname = forname.replace("Ö","O");
        forname = forname.replace("ä","a");
        forname = forname.replace("Ä","A");
        return forname;
    }
}
