package liveKanot.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import liveKanot.UiController.SettingsController;
import liveKanot.entities.Race;
import liveKanot.utils.YouTubeChapterInfo;
import org.json.JSONArray;

import java.time.LocalDateTime;
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
            return MockData.getMockRaceRandom(removeÅÄÖ, raceNumber);
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

    public void puchChapterData(SettingsController settings, int raceNumber, String info, String raceClass, String distance) throws UnirestException, JsonProcessingException {
        YouTubeChapterInfo youTubeChapterInfo = new YouTubeChapterInfo(
                settings.getCompetition(),
                null,
                raceNumber,
                info,
                LocalDateTime.now(),
                raceClass,
                distance
        );
        ObjectMapper objectMapper = new ObjectMapper()
//                .registerModule(new ParameterNamesModule())
//                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        ;

        String baseUrl = settings.getUrl();
        String body = objectMapper.writeValueAsString(youTubeChapterInfo);
        HttpResponse<String> stringHttpResponse = Unirest
                .post((baseUrl.endsWith("/") ? baseUrl : baseUrl + "/") + "api/chapter/" + settings.getCompetition() + "?apiKey=" + settings.apiKey.getText())
                .header("Content-Type", "application/json")
                .body(body)
                .asString();
        System.out.println("Response: " + stringHttpResponse.getBody());
    }

    public JsonNode fetchUrlToJson(String url) throws UnirestException {

        HttpResponse<JsonNode> jsonResponse
                = Unirest.get(url)
                .asJson();

        return jsonResponse.getBody();
    }
}
