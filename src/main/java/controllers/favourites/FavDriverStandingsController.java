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
import serializableClasses.DriverStandingData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class FavDriverStandingsController extends FavController implements Initializable {
    @FXML
    private TableView<DriverStandingData> tableView;
    @FXML
    private JFXListView<String> listView;

    private TableColumn<DriverStandingData, Integer> columnOne;
    private TableColumn<DriverStandingData, String> columnTwo;
    private TableColumn<DriverStandingData, Integer> columnThree;

    private HashMap<String, List<DriverStandingData>> driversDataMap;
    private ObservableList<DriverStandingData> driverStandingsObservableList;
    private List<DriverStandingData> driverStandings;
    private List<String> positions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onStart();
    }

    protected void initializeContainers() {
        driverStandingsObservableList = FXCollections.observableArrayList();
        driverStandings = new ArrayList<>();
        driversDataMap = new HashMap<>();
        positions = new ArrayList<>();
    }

    protected void loadSerializedData() {
        String path = "driversData.ser";
        File serializedFile = new File(path);
        if (serializedFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(path);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                driversDataMap = (HashMap) objectInputStream.readObject();
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
        if (!driversDataMap.isEmpty()) {
            positions.addAll(driversDataMap.keySet());
            listView.getItems().addAll(positions);
        }
    }

    @FXML
    protected void passDataIntoTableView(MouseEvent mouseEvent) {
        loadDataToTable(listView.getSelectionModel().getSelectedItem());
    }

    protected void createTableViewColumns() {
        columnOne = new TableColumn<>("Pozycja");
        columnTwo = new TableColumn<>("Kierowca");
        columnThree = new TableColumn<>("Punkty");
        columnOne.getStyleClass().add("column-style2");
        columnTwo.getStyleClass().add("column-style");
        columnThree.getStyleClass().add("column-style2");
        tableView.getColumns().addAll(columnOne, columnTwo, columnThree);
    }

    protected void loadDataToTable(String selectedPositon) {
        tableView.getItems().clear();
        getDataForSelectedPosition(selectedPositon);
        driverStandingsObservableList.addAll(driverStandings);
        columnOne.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPosition()));
        columnTwo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDriverName()));
        columnThree.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPoints()));
        tableView.setItems(driverStandingsObservableList);
    }

    protected void getDataForSelectedPosition(String selectedPosition) {
        if (selectedPosition != null) {
            driverStandings = driversDataMap.get(selectedPosition);
        }
    }
}
