package liveKanot.data;

import liveKanot.entities.Bana;
import liveKanot.entities.Race;

import java.util.ArrayList;
import java.util.List;

public class MockData {

    public static Race getMockData(String raceNumber, String competition, boolean removeÅÄÖ){
        System.out.println("Fetching mock data");
        return new Race(getMockBanor(removeÅÄÖ),"H221","försök",1,"200",raceNumber);
    }


    private static List<Bana> getMockBanor(boolean removeÅÄÖ){
        ArrayList<Bana> banor = new ArrayList<>();

        banor.add(new Bana("1","Klas " + (removeÅÄÖ? "Goran": "Göran"),"40.3","1","klubb Paddling"));
        banor.add(new Bana("2","Malm " + (removeÅÄÖ? "Goran": "Göran"),"41.4","2","klubb Paddling"));
        banor.add(new Bana("3","Gustav " + (removeÅÄÖ? "Goran": "Göran"),"43.2","3","klubb Paddling"));
        banor.add(new Bana("4","Glenn " + (removeÅÄÖ? "Goran": "Göran"),"43.2","4","klubb Paddling"));
        banor.add(new Bana("5","Sven " + (removeÅÄÖ? "Goran": "Göran"),"43.2","5","klubb Paddling"));
        banor.add(new Bana("6","Martin " + (removeÅÄÖ? "Goran": "Göran"),"43.2","6","klubb Paddling"));

        return banor;
    }

}
