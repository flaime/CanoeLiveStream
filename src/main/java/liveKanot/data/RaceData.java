package liveKanot.data;

import liveKanot.Controller;
import liveKanot.entities.Bana;
import liveKanot.entities.Race;

import java.util.ArrayList;
import java.util.Comparator;

public class RaceData {

    public static String getHeaderText(Race race,Controller backReference){

        final String raceClass = normaliseRaceClass(race.getRaceClass());
        final String type = backReference.normaliseType(race.getType());

        return "Heat " + race.getRaceNumber() + " " + raceClass + " " + race.getDistance() + "m " + type + " " + race.getTypeNumber();
    }

    public static String getRacesJson(Race race) {
        ArrayList<Bana> banor = race.getBanor();

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
}
