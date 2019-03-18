package serializableClasses;
import java.io.Serializable;

public class ConstructorStandingData implements Serializable {
    private Integer position;
    private String name;
    private String nationality;
    private Integer points;

    public ConstructorStandingData(Integer position, String name, String nationality, Integer points) {
        this.position = position;
        this.name = name;
        this.nationality = nationality;
        this.points = points;
    }

    public Integer getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public Integer getPoints() {
        return points;
    }
}
