package serializableClasses;
import java.io.Serializable;

public class QualifyData implements Serializable {
    private Integer position;
    private String driverName;
    private String constructorName;
    private String q1Result;
    private String q2Result;
    private String q3Result;

    public QualifyData(Integer position, String driverName, String constructorName, String q1Result, String q2Result, String q3Result) {
        this.position = position;
        this.driverName = driverName;
        this.constructorName = constructorName;
        this.q1Result = q1Result;
        this.q2Result = q2Result;
        this.q3Result = q3Result;
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

    public String getQ1Result() {
        return q1Result;
    }

    public String getQ2Result() {
        return q2Result;
    }

    public String getQ3Result() {
        return q3Result;
    }
}
