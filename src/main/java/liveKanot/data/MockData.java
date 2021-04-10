package liveKanot.data;

import liveKanot.entities.Bana;
import liveKanot.entities.Race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class MockData {


    public static List<Race> getMultipleMockRaceRandom(String competition, boolean removeÅÄÖ, int number) {
        return IntStream.range(0, number).mapToObj(it -> getMockRaceRandom(null, competition, removeÅÄÖ)).collect(toList());
    }

    public static Race getMockRaceRandom(String raceNumber, String competition, boolean removeÅÄÖ) {
        System.out.println("Fetching mock data");
        return new Race(
                getMockBanor(removeÅÄÖ),
                "H221",
                types.get(getRandomBetween(0, types.size())),
                getRandomBetween(0, 4),
                distances.get(getRandomBetween(0, distances.size())),
                raceNumber == null ? String.valueOf((getRandomBetween(1, 500))) : raceNumber);
    }

    private static int getRandomBetween(int from, int to) {
        return new Random().nextInt(from) + to;
    }

    static List<String> distances = Arrays.asList("200", "500", "1000", "5000");
    static List<String> types = Arrays.asList("försök", "final", "mellanhet");

    private static List<Bana> getMockBanor(boolean removeÅÄÖ) {
        ArrayList<Bana> banor = new ArrayList<>();

        banor.add(new Bana("1", "Klas " + (removeÅÄÖ ? "Goran" : "Göran"), "40.3", "1", "klubb Paddling"));
        banor.add(new Bana("2", "Malm " + (removeÅÄÖ ? "Goran" : "Göran"), "41.4", "2", "klubb Paddling"));
        banor.add(new Bana("3", "Gustav " + (removeÅÄÖ ? "Goran" : "Göran"), "43.2", "3", "klubb Paddling"));
        banor.add(new Bana("4", "Glenn " + (removeÅÄÖ ? "Goran" : "Göran"), "43.2", "4", "klubb Paddling"));
        banor.add(new Bana("5", "Sven " + (removeÅÄÖ ? "Goran" : "Göran"), "43.2", "5", "klubb Paddling"));
        banor.add(new Bana("6", "Martin " + (removeÅÄÖ ? "Goran" : "Göran"), "43.2", "6", "klubb Paddling"));

        return banor;
    }

}
