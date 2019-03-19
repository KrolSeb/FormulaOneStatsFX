package controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ru.olerom.formula.ergast.Ergast;
import ru.olerom.formula.ergast.objects.DriverStandings;
import serializableClasses.DriverStandingData;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class DriversController extends Controller implements Initializable{
    @FXML
    private TableView<DriverStandingData> tableView;

    private TableColumn<DriverStandingData, Integer> positionColumn;
    private TableColumn<DriverStandingData, String> driverNameColumn;
    private TableColumn<DriverStandingData, Integer> pointsColumn;

    private ObservableList<DriverStandingData> driverStandingsObservableList;
    private List<DriverStandings> driverStandingsFromAPI;
    private List<DriverStandingData> driverStandings;
    private HashMap<String,List<DriverStandingData>> driversDataMap;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onStart();
    }

    protected void initializeLists() {
        driverStandingsObservableList = FXCollections.observableArrayList();
        driverStandingsFromAPI = new ArrayList<>();
        driverStandings = new ArrayList<>();
        driversDataMap = new HashMap<>();
    }

    protected void getData() {
        driverStandings = new ArrayList<>();
        Ergast ergast = new Ergast(getSeason(), 40, Ergast.DEFAULT_OFFSET);

        try {
            driverStandingsFromAPI = ergast.getDriverStandings(getRoundNumber());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (DriverStandings driverStanding : driverStandingsFromAPI) {
            driverStandings.add(new DriverStandingData(driverStanding.getPosition(),
                    driverStanding.getDriver().getGivenName() + " " + driverStanding.getDriver().getFamilyName(),
                    driverStanding.getPoints()));
        }
    }

    protected void loadDataToTable(){
        tableView.getItems().clear();
        driverStandingsObservableList.addAll(driverStandings);

        positionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPosition()));
        driverNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDriverName()));
        pointsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPoints()));

        tableView.setItems(driverStandingsObservableList);
    }

    protected void createTableViewColumns() {
        positionColumn = new TableColumn<>("Pozycja");
        driverNameColumn = new TableColumn<>("Kierowca");
        pointsColumn = new TableColumn<>("Punkty");

        positionColumn.getStyleClass().add("column-style2");
        driverNameColumn.getStyleClass().add("column-style");
        pointsColumn.getStyleClass().add("column-style2");

        tableView.getColumns().addAll(positionColumn, driverNameColumn, pointsColumn);
    }

    @FXML
    private void serializeData(ActionEvent actionEvent) {
        try{
            FileOutputStream fileOutputStream= new FileOutputStream("driversData.ser");
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
            driversDataMap.put(generateListViewKeys(),driverStandings);
            objectOutputStream.writeObject(driversDataMap);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void saveDataToFile(ActionEvent actionEvent) throws IOException {
        String path = "DriverStandings_" + getSeason() + "_round_" + getRoundNumber() + ".txt";
        FileWriter writer = new FileWriter(path);
        for(DriverStandingData driverStanding: driverStandings) {
            writer.write(driverStanding.getPosition() + " " + driverStanding.getDriverName() + " " + driverStanding.getPoints());
            writer.write("\n");
        }
        writer.close();
    }
}