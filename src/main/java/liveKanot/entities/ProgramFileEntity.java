package liveKanot.entities;

/**
 * Not that this file has a very specific objective and that is to have an object to create an json-file similar to this:
 * <p>
 * [{"Heat": "1\n2\n3\n4\n5\n6\n7\n8\n9\n10",
 * "Dag_Tid": "2017-07-17 10:00\n2017-07-17 10:03\n2017-07-17 10:06\n2017-07-17 10:09\n2017-07-17 10:12\n2017-07-17 10:15\n2017-07-17 10:18\n2017-07-17 10:21\n2017-07-17 10:24\n2017-07-17 10:30",
 * "Klass": "H161\nH161\nH161\nH161\nH161\nD141\nD141\nD141\nD141\nH141",
 * "Distans": "500\n500\n500\n500\n500\n500\n500\n500\n500\n500",
 * "Typ": "Försök 1\nFörsök 2\nFörsök 3\nFörsök 4\nFörsök 5\nFörsök 1\nFörsök 2\nMellan heat 1\nMellan heat 2\nMellan heat 3"}]
 */
public class ProgramFileEntity {
    String Heat;
    String Dag_Tid;
    String Klass;
    String Distans;
    String Typ;

    public ProgramFileEntity() {
    }

    public ProgramFileEntity(String heat, String dag_Tid, String klass, String distans, String typ) {
        Heat = heat;
        Dag_Tid = dag_Tid;
        Klass = klass;
        Distans = distans;
        Typ = typ;
    }

    public String getHeat() {
        return Heat;
    }

    public void setHeat(String heat) {
        Heat = heat;
    }

    public String getDag_Tid() {
        return Dag_Tid;
    }

    public void setDag_Tid(String dag_Tid) {
        Dag_Tid = dag_Tid;
    }

    public String getKlass() {
        return Klass;
    }

    public void setKlass(String klass) {
        Klass = klass;
    }

    public String getDistans() {
        return Distans;
    }

    public void setDistans(String distans) {
        Distans = distans;
    }

    public String getTyp() {
        return Typ;
    }

    public void setTyp(String typ) {
        Typ = typ;
    }
}
