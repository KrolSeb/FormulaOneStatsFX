package serializableClasses;
import java.io.Serializable;

public class DriverStandingData implements Serializable {
    private Integer position;
    private String driverName;
    private Integer points;

    public DriverStandingData(Integer position, String driverName, Integer points) {
        this.position = position;
        this.driverName = driverName;
        this.points = points;
    }

    public Integer getPosition() {
        return position;
    }

    public String getDriverName() {
        return driverName;
    }

    public Integer getPoints() {
        return points;
    }
}
