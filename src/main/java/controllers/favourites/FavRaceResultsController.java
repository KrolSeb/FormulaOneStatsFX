package controllers.favourites;

import com.jfoenix.controls.JFXListView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import serializableClasses.RaceData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class FavRaceResultsController extends FavController implements Initializable {
    @FXML
    private TableView<RaceData> tableView;
    @FXML
    private JFXListView<String> listView;

    private TableColumn<RaceData, Integer> positionColumn;
    private TableColumn<RaceData, String> driverNameColumn;
    private TableColumn<RaceData, String> constructorNameColumn;
    private TableColumn<RaceData, Integer> lapsColumn;
    private TableColumn<RaceData, Integer> startPositionColumn;

    private HashMap<String,List<RaceData>> raceResultsMap;
    private ObservableList<RaceData> raceResultsObservableList;
    private List<RaceData> raceResults;
    private List<String> positions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeContainers();
        createTableViewColumns();
        loadSerializedData();
        fillListViewWithItems();
    }

    protected void initializeContainers() {
        raceResultsObservableList = FXCollections.observableArrayList();
        raceResults = new ArrayList<>();
        raceResultsMap = new HashMap<>();
        positions = new ArrayList<>();
    }

    protected void loadSerializedData() {
        String path = "raceResults.ser";
        File serializedFile = new File(path);
        if (serializedFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(path);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                raceResultsMap = (HashMap) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    protected void fillListViewWithItems() {
        if (!raceResultsMap.isEmpty()) {
            positions.addAll(raceResultsMap.keySet());
            listView.getItems().addAll(positions);
        }
    }

    @FXML
    protected void passDataIntoTableView(MouseEvent mouseEvent) {
        loadDataToTable(listView.getSelectionModel().getSelectedItem());
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

    protected void loadDataToTable(String selectedPositon) {
        tableView.getItems().clear();
        getDataForSelectedPosition(selectedPositon);
        raceResultsObservableList.addAll(raceResults);
        positionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPosition()));
        driverNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDriverName()));
        constructorNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getConstructorName()));
        lapsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getLaps()));
        startPositionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getStartPosition()));
        tableView.setItems(raceResultsObservableList);
    }

    protected void getDataForSelectedPosition(String selectedPosition) {
        if (selectedPosition != null) {
            raceResults = raceResultsMap.get(selectedPosition);
        }
    }
}
