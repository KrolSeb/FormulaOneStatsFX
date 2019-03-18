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
import ru.olerom.formula.ergast.objects.ConstructorStandings;
import serializableClasses.ConstructorStandingData;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ConstructorsController extends Controller implements Initializable {
    @FXML
    private TableView<ConstructorStandingData> tableView;

    private TableColumn<ConstructorStandingData, Integer> positionColumn;
    private TableColumn<ConstructorStandingData, String> constructorNameColumn;
    private TableColumn<ConstructorStandingData, String> nationalityColumn;
    private TableColumn<ConstructorStandingData, Integer> pointsColumn;

    private ObservableList<ConstructorStandingData> constructorStandingsObservableList;
    private List<ConstructorStandings> constructorStandingsFromAPI;
    private List<ConstructorStandingData> constructorStandings;
    private HashMap<String,List<ConstructorStandingData>> constructorsDataMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onStart();
    }

    protected void initializeLists() {
        constructorStandingsObservableList = FXCollections.observableArrayList();
        constructorStandingsFromAPI = new ArrayList<>();
        constructorStandings = new ArrayList<>();
        constructorsDataMap = new HashMap<>();
    }

    protected void getData() {
        constructorStandings = new ArrayList<>();
        Ergast ergast = new Ergast(getSeason(), 40, Ergast.DEFAULT_OFFSET);

        try {
            constructorStandingsFromAPI = ergast.getConstructorStandings(getRoundNumber());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (ConstructorStandings constructorStanding: constructorStandingsFromAPI) {
            constructorStandings.add(new ConstructorStandingData(constructorStanding.getPosition(),
                    constructorStanding.getConstructor().getName(),
                    constructorStanding.getConstructor().getNationality(), constructorStanding.getPoints()));
        }
    }

    protected void loadDataToTable(){
        tableView.getItems().clear();
        constructorStandingsObservableList.addAll(constructorStandings);

        positionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPosition()));
        constructorNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        nationalityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNationality()));
        pointsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPoints()));

        tableView.setItems(constructorStandingsObservableList);
    }

    protected void createTableViewColumns() {
        positionColumn = new TableColumn<>("Pozycja");
        constructorNameColumn = new TableColumn<>("Zespół");
        nationalityColumn = new TableColumn<>("Kraj pochodzenia");
        pointsColumn = new TableColumn<>("Punkty");

        positionColumn.getStyleClass().add("column-style2");
        constructorNameColumn.getStyleClass().add("column-style");
        nationalityColumn.getStyleClass().add("column-style");
        pointsColumn.getStyleClass().add("column-style2");

        tableView.getColumns().addAll(positionColumn, constructorNameColumn, nationalityColumn,pointsColumn);
    }

    @FXML
    private void serializeData(ActionEvent actionEvent) {
        try{
            FileOutputStream fileOutputStream= new FileOutputStream("constructorsData.ser");
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
            constructorsDataMap.put(generateListViewKeys(),constructorStandings);
            objectOutputStream.writeObject(constructorsDataMap);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void saveDataToFile(ActionEvent actionEvent) throws IOException {
        String path = "ConstructorStandings_" + getSeason() + "_round_" + getRoundNumber() + ".txt";
        FileWriter writer = new FileWriter(path);
        for(ConstructorStandingData constructorStanding: constructorStandings) {
            writer.write(constructorStanding.getPosition() + " " + constructorStanding.getName() + " " + constructorStanding.getNationality() + " " + constructorStanding.getPoints());
            writer.write("\n");
        }
        writer.close();
    }
}