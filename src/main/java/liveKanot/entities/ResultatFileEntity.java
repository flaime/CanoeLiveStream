package liveKanot.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import liveKanot.utils.CustomLocalDateTimeSerializer;

import java.time.LocalDateTime;

public class ResultatFileEntity {

    String lopp;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    LocalDateTime startTid;
    String beskrivning; //h18 k1
    String distans;
    String Klartext;// (Försök 1, Final etc.)
    String bana;
    String besättning; // (namn1 – namn4)
    String klubb; // Programnamn

    public ResultatFileEntity(String lopp, LocalDateTime startTid, String beskrivning, String distans, String klartext, String bana, String besättning, String klubb) {
        this.lopp = lopp;
        this.startTid = startTid;
        this.beskrivning = beskrivning;
        this.distans = distans;
        Klartext = klartext;
        this.bana = bana;
        this.besättning = besättning;
        this.klubb = klubb;
    }

    public String getLopp() {
        return lopp;
    }

    public void setLopp(String lopp) {
        this.lopp = lopp;
    }

    public LocalDateTime getStartTid() {
        return startTid;
    }

    public void setStartTid(LocalDateTime startTid) {
        this.startTid = startTid;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public String getDistans() {
        return distans;
    }

    public void setDistans(String distans) {
        this.distans = distans;
    }

    public String getKlartext() {
        return Klartext;
    }

    public void setKlartext(String klartext) {
        Klartext = klartext;
    }

    public String getBana() {
        return bana;
    }

    public void setBana(String bana) {
        this.bana = bana;
    }

    public String getBesättning() {
        return besättning;
    }

    public void setBesättning(String besättning) {
        this.besättning = besättning;
    }

    public String getKlubb() {
        return klubb;
    }

    public void setKlubb(String klubb) {
        this.klubb = klubb;
    }
}
