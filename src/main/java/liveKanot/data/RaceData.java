package liveKanot.data;

import liveKanot.UiController.MainController;
import liveKanot.UiController.SettingsController;
import liveKanot.entities.Bana;
import liveKanot.entities.Race;

import java.util.Comparator;
import java.util.List;

public class RaceData {

    public static String getHeaderText(Race race, SettingsController settings) {

        final String raceClass = race.normaliseRaceClass();
        final String type = race.normaliseType(settings);

        return "Heat " + race.getRaceNumber() + " " + raceClass + " " + race.getDistance() + "m " + type + " " + race.getTypeNumber();
    }

    public static String getRacesJsonBulkar(Race race) {
        List<Bana> banor = race.getBanor();

        final boolean timeExist = sortPrimaryByTimeElseByBanaNr(banor);

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

    public static String racesJson(Race race, int limit, int offset) {
        List<Bana> banor = race.getBanor();
        sortPrimaryByTimeElseByBanaNr(banor);

        String jsonHokus = "{ \"bulkar\" : [ {";

        for (int i = offset * limit; i < offset * limit + limit && i < banor.size(); i++) {
            Bana bana = banor.get(i);
            jsonHokus += "\"person" +  (i - offset * limit) + "Lopp\": \"" + race.getRaceNumber() + "\",\n";
//            jsonHokus += "\"person" +  (i - offset * limit) + "StartTid\": \"" + race.get + "\",\n";
            jsonHokus += "\"person" +  (i - offset * limit) + "Name\": \"" + bana.getNamn() + "\",\n";
        }
        jsonHokus += "}]}";
        return jsonHokus;
    }
    private static boolean sortPrimaryByTimeElseByBanaNr(List<Bana> banor) {
        banor.sort(Comparator.comparing(Bana::getBana));
        final boolean timeExist = banor.stream()
                .anyMatch(bana -> !bana.getTid().equals("null"));
        if(timeExist)
            banor.sort(Comparator.comparing(Bana::getTid));
        return timeExist;
    }
}
