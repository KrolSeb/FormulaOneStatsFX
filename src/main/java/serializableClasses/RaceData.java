package serializableClasses;
import java.io.Serializable;

public class RaceData implements Serializable {
    private Integer position;
    private String driverName;
    private String constructorName;
    private Integer laps;
    private Integer startPosition;

    public RaceData(Integer position, String driverName, String constructorName, Integer laps, Integer startPosition) {
        this.position = position;
        this.driverName = driverName;
        this.constructorName = constructorName;
        this.laps = laps;
        this.startPosition = startPosition;
    }

    public Integer getPosition() {
        return position;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getConstructorName() {
        return constructorName;
    }

    public Integer getLaps() {
        return laps;
    }

    public Integer getStartPosition() {
        return startPosition;
    }
}
