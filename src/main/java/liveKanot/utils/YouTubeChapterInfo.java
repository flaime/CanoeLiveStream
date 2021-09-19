package liveKanot.utils;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class YouTubeChapterInfo {

    public YouTubeChapterInfo(String competitionName, Long competitionId, int raceNummer, String info, LocalDateTime time, String klass, String distans) {
        this.competitionName = competitionName;
        this.competitionId = competitionId;
        this.raceNummer = raceNummer;
        this.info = info;
        this.time = time;
        this.klass = klass;
        this.distans = distans;
    }

    private Long id = null;

    private String competitionName;

    private Long competitionId;

    private int raceNummer = -1;

    private String info;


    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time = LocalDateTime.of(1900, 1, 1, 1, 1, 1);

    private String klass;

    private String distans;

    public YouTubeChapterInfo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public Long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }

    public int getRaceNummer() {
        return raceNummer;
    }

    public void setRaceNummer(int raceNummer) {
        this.raceNummer = raceNummer;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getKlass() {
        return klass;
    }

    public void setKlass(String klass) {
        this.klass = klass;
    }

    public String getDistans() {
        return distans;
    }

    public void setDistans(String distans) {
        this.distans = distans;
    }
}
