package liveKanot.entities;

import liveKanot.UiController.SettingsController;
import liveKanot.data.MockData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RaceTest {

    @Test
    void normaliseTypeFÖ() {
        //For
        Race race = MockData.getMockRaceRandom(null, null, null, null, "FÖ", null, null);
        SettingsController settings = Mockito.mock(SettingsController.class);
        Mockito.when(settings.getFörsök()).thenReturn("försök");
        //when
        String normalisedFörsök = race.normaliseType(settings);
        //then
        Assertions.assertEquals("försök", normalisedFörsök);
    }

    @Test
    void normaliseTypeAF() {
        //For
        Race race = MockData.getMockRaceRandom(null, null, null, null, "AF", null, null);
        SettingsController settings = Mockito.mock(SettingsController.class);
        Mockito.when(settings.getAFinal()).thenReturn("A-Final");
        //when
        String normalisedFörsök = race.normaliseType(settings);
        //then
        Assertions.assertEquals("A-Final", normalisedFörsök);
    }

    @Test
    void normaliseTypeBF() {
        //For
        Race race = MockData.getMockRaceRandom(null, null, null, null, "BF", null, null);
        SettingsController settings = Mockito.mock(SettingsController.class);
        Mockito.when(settings.getBFinal()).thenReturn("B-Final");
        //when
        String normalisedFörsök = race.normaliseType(settings);
        //then
        Assertions.assertEquals("B-Final", normalisedFörsök);
    }

    @Test
    void normaliseTypeCF() {
        //For
        Race race = MockData.getMockRaceRandom(null, null, null, null, "CF", null, null);
        SettingsController settings = Mockito.mock(SettingsController.class);
        Mockito.when(settings.getFinalÖvrigt()).thenReturn("Final");
        //when
        String normalisedFörsök = race.normaliseType(settings);
        //then
        Assertions.assertEquals("C-Final", normalisedFörsök);
    }
}