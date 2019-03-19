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
import serializableClasses.ConstructorStandingData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class FavConstructorStandingsController extends FavController implements Initializable {
    @FXML
    private TableView<ConstructorStandingData> tableView;
    @FXML
    private JFXListView<String> listView;

    private TableColumn<ConstructorStandingData, Integer> positionColumn;
    private TableColumn<ConstructorStandingData, String> constructorNameColumn;
    private TableColumn<ConstructorStandingData, String> nationalityColumn;
    private TableColumn<ConstructorStandingData, Integer> pointsColumn;

    private HashMap<String,List<ConstructorStandingData>> constructorsDataMap;
    private ObservableList<ConstructorStandingData> constructorStandingsObservableList;
    private List<ConstructorStandingData> constructorStandings;
    private List<String> positions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onStart();
    }

    protected void initializeContainers() {
        constructorStandingsObservableList = FXCollections.observableArrayList();
        constructorStandings = new ArrayList<>();
        constructorsDataMap = new HashMap<>();
        positions = new ArrayList<>();
    }

    protected void loadSerializedData() {
        String path = "constructorsData.ser";
        File serializedFile = new File(path);
        if (serializedFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(path);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                constructorsDataMap = (HashMap) objectInputStream.readObject();
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
        if (!constructorsDataMap.isEmpty()) {
            positions.addAll(constructorsDataMap.keySet());
            listView.getItems().addAll(positions);
        }
    }

    @FXML
    protected void passDataIntoTableView(MouseEvent mouseEvent) {
        loadDataToTable(listView.getSelectionModel().getSelectedItem());
    }

    protected void createTableViewColumns() {
        positionColumn = new TableColumn<>("Pozycja");
        constructorNameColumn = new TableColumn<>("Zespół");
        nationalityColumn = new TableColumn<>("Kraj pochodzenia");
        pointsColumn = new TableColumn<>("Punkty");

        positionColumn.getStyleClass().add("column-style2");
        constructorNameColumn.getStyleClass().add("column-style");
        constructorNameColumn.getStyleClass().add("column-style");
        nationalityColumn.getStyleClass().add("column-style2");

        tableView.getColumns().addAll(positionColumn, constructorNameColumn, nationalityColumn, pointsColumn);
    }

    protected void loadDataToTable(String selectedPositon) {
        tableView.getItems().clear();
        getDataForSelectedPosition(selectedPositon);
        constructorStandingsObservableList.addAll(constructorStandings);
        positionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPosition()));
        constructorNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        nationalityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNationality()));
        pointsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPoints()));
        tableView.setItems(constructorStandingsObservableList);
    }

    protected void getDataForSelectedPosition(String selectedPosition) {
        if (selectedPosition != null) {
            constructorStandings = constructorsDataMap.get(selectedPosition);
        }
    }

}
