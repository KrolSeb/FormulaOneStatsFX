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
import ru.olerom.formula.ergast.objects.RaceResult;
import serializableClasses.RaceData;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class RaceResultsController extends Controller implements Initializable {
    @FXML
    private TableView<RaceData> tableView;

    private TableColumn<RaceData, Integer> positionColumn;
    private TableColumn<RaceData, String> driverNameColumn;
    private TableColumn<RaceData, String> constructorNameColumn;
    private TableColumn<RaceData, Integer> lapsColumn;
    private TableColumn<RaceData, Integer> startPositionColumn;
    
    private ObservableList<RaceData> raceResultsObservableList;
    private List<RaceResult> raceResultsFromAPI;
    private List<RaceData> raceResults;
    private HashMap<String,List<RaceData>> raceResultsMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onStart();
    }

    protected void initializeLists() {
        raceResultsObservableList = FXCollections.observableArrayList();
        raceResultsFromAPI = new ArrayList<>();
        raceResults = new ArrayList<>();
        raceResultsMap = new HashMap<>();
    }

    protected void getData() {
        raceResults = new ArrayList<>();
        Ergast ergast = new Ergast(getSeason(), 40, Ergast.DEFAULT_OFFSET);

        try {
            raceResultsFromAPI = ergast.getRaceResults(getRoundNumber());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (RaceResult raceResult: raceResultsFromAPI) {
            raceResults.add(new RaceData(raceResult.getPosition(), raceResult.getDriver().getGivenName() + " " + raceResult.getDriver().getFamilyName(),
                    raceResult.getConstructor().getName(), raceResult.getLaps(), raceResult.getGrid()));
        }
    }

    protected void loadDataToTable(){
        tableView.getItems().clear();
        raceResultsObservableList.addAll(raceResults);
        positionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPosition()));
        driverNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDriverName()));
        constructorNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getConstructorName()));
        lapsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getLaps()));
        startPositionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getStartPosition()));

        tableView.setItems(raceResultsObservableList);
    }

    protected void createTableViewColumns() {
        positionColumn = new TableColumn<>("Pozycja");
        driverNameColumn = new TableColumn<>("Kierowca");
        constructorNameColumn = new TableColumn<>("Zespół");
        lapsColumn = new TableColumn<>("Liczba okrążeń");
        startPositionColumn = new TableColumn<>("Pozycja startowa");

        positionColumn.getStyleClass().add("column-style2");
        driverNameColumn.getStyleClass().add("column-style");
        constructorNameColumn.getStyleClass().add("column-style");
        lapsColumn.getStyleClass().add("column-style");
        startPositionColumn.getStyleClass().add("column-style2");

        tableView.getColumns().addAll(positionColumn, driverNameColumn, constructorNameColumn, lapsColumn, startPositionColumn);
    }

    @FXML
    private void serializeData(ActionEvent actionEvent) {
        try{
            FileOutputStream fileOutputStream= new FileOutputStream("raceResults.ser");
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
            raceResultsMap.put(generateListViewKeys(),raceResults);
            objectOutputStream.writeObject(raceResultsMap);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void saveDataToFile(ActionEvent actionEvent) throws IOException {
        String path = "RaceResults_" + getSeason() + "_round_" + getRoundNumber() + ".txt";
        FileWriter writer = new FileWriter(path);
        for(RaceData raceData: raceResults) {
            writer.write(raceData.getPosition() + " " + raceData.getDriverName() + " " + raceData.getConstructorName() + " " + raceData.getLaps() + " " + raceData.getStartPosition());
            writer.write("\n");
        }
        writer.close();
    }
}
