package liveKanot.data;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import liveKanot.entities.Race;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class DataFetcher {


    public List<Race> getRaces(String competition, String baseUrl, boolean removeÅÄÖ) throws UnirestException {
        if (competition.equalsIgnoreCase("testdata"))
            return MockData.getMultipleMockRaceRandom(competition, removeÅÄÖ, 40);
        else
            return getRacesInternal(competition, baseUrl, removeÅÄÖ);
    }

    public Race getRace(String raceNumber, String competition, String baseUrl, boolean removeÅÄÖ) throws UnirestException {
        if (competition.equalsIgnoreCase("testdata"))
            return MockData.getMockRaceRandom(raceNumber, competition, removeÅÄÖ);
        else
            return new Race(getRaceJsonNode(raceNumber, competition, baseUrl).getObject(), raceNumber, removeÅÄÖ);
    }

    public List<Race> getRacesInternal(String competition, String baseUrl, boolean removeÅÄÖ) throws UnirestException {
        String url = (baseUrl.endsWith("/") ? baseUrl : baseUrl + "/") + "api/competitions/" + competition;

        System.out.println("Url: " + url);

        final JsonNode races = fetchUrlToJson(url);

        JSONArray races1 = races.getObject().getJSONArray("races");
        List<Race> racesRet = new ArrayList<>(races1.length());
        for (int i = 0; i < races1.length(); i++) {
            racesRet.add(new Race(races1.getJSONObject(i), races1.getJSONObject(i).getInt("raceNummer") + "", removeÅÄÖ));
        }
        return racesRet;
    }

    public JsonNode getRaceJsonNode(String raceNumber, String competition, String baseUrl) throws UnirestException {
        String url = (baseUrl.endsWith("/") ? baseUrl : baseUrl + "/") + "api/competitions/" + competition + "/" + raceNumber;

        System.out.println("Url: " + url);

        final JsonNode races = fetchUrlToJson(url);

        return races;
    }


    public JsonNode fetchUrlToJson(String url) throws UnirestException {

        HttpResponse<JsonNode> jsonResponse
                = Unirest.get(url)
                .asJson();

        return jsonResponse.getBody();
    }
}
