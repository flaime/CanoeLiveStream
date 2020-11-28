package liveKanot.entities;

public class Bana {
    String placering;
    String namn;
    String tid;
    String bana;
    String clubb;

    public Bana(String placering, String namn, String tid, String bana,String clubb) {
        this.placering = placering;
        this.namn = namn;
        this.tid = tid;
        this.bana = bana;
        this.clubb = clubb;
    }

    public String getPlacering() {
        return placering;
    }

    public void setPlacering(String placering) {
        this.placering = placering;
    }

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getBana() {
        return bana;
    }

    public void setBana(String bana) {
        this.bana = bana;
    }

    public String getClubb() {
        return clubb;
    }

    public void setClubb(String clubb) {
        this.clubb = clubb;
    }
}
