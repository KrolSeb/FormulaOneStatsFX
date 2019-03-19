package serializableClasses;
import java.io.Serializable;

public class GPInformationData implements Serializable {
    private Integer round;
    private String country;
    private String locality;
    private String circuitName;
    private String date;
    private String time;

    public GPInformationData(Integer round, String country, String locality, String circuitName, String date, String time) {
        this.round = round;
        this.country = country;
        this.locality = locality;
        this.circuitName = circuitName;
        this.date = date;
        this.time = time;
    }

    public Integer getRound() {
        return round;
    }

    public String getCountry() {
        return country;
    }

    public String getLocality() {
        return locality;
    }

    public String getCircuitName() {
        return circuitName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

}
