package liveKanot.data;

import liveKanot.entities.Bana;
import liveKanot.entities.Race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class MockData {


    public static List<Race> getMultipleMockRaceRandom(String competition, boolean removeÅÄÖ, int number) {
        return IntStream.range(0, number).mapToObj(it -> getMockRaceRandom(removeÅÄÖ, null)).collect(toList());
    }

    public static Race getMockRaceRandom(boolean removeÅÄÖ, String raceNumber) {
        return getMockRaceRandom(raceNumber, removeÅÄÖ, null, null, null, null, null);
    }

    public static Race getMockRaceRandom(String raceNumber, Boolean removeÅÄÖ, List<Bana> banor, String raceClass, String type, Integer typeNumber, String distanc) {
        System.out.println("Fetching mock data");
        return new Race(
                banor == null ? getMockBanor(removeÅÄÖ == null ? false : removeÅÄÖ) : banor,
                raceClass == null ? "H221" : raceClass,
                type == null ? types.get(getRandomBetween(0, types.size())) : type,
                typeNumber == null ? getRandomBetween(0, 4) : typeNumber,
                distanc == null ? distances.get(getRandomBetween(0, distances.size() - 1)) : distanc,
                raceNumber == null ? String.valueOf((getRandomBetween(1, 500))) : raceNumber);
    }

    private static int getRandomBetween(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to + 1);
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
