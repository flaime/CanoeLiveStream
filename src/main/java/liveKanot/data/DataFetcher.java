package liveKanot.data;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import liveKanot.entities.Race;

public class DataFetcher {

    public Race getRace(String raceNumber, String competition, String baseUrl, boolean removeÅÄÖ) throws UnirestException {
        if(competition.equals("test"))
            return MockData.getMockData(raceNumber,competition,removeÅÄÖ);
        else
            return new Race(getRaceJsonNode(raceNumber, competition, baseUrl),raceNumber, removeÅÄÖ);
    }
    public JsonNode getRaceJsonNode(String raceNumber, String competition, String baseUrl) throws UnirestException {
        String url = baseUrl + "/api/competitions/" + competition + "/" + raceNumber;

        System.out.println("Url: " + url);

        final JsonNode races = fetchUrlToJson(url);
        System.out.println(races);

        return races;
    }


    public JsonNode fetchUrlToJson(String url) throws UnirestException {

        HttpResponse<JsonNode> jsonResponse
                = Unirest.get(url)
                .asJson();

        return jsonResponse.getBody();
    }
}
