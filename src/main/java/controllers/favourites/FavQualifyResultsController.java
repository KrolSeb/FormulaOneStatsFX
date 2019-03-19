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
import serializableClasses.QualifyData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class FavQualifyResultsController extends FavController implements Initializable {
    @FXML
    private TableView<QualifyData> tableView;
    @FXML
    private JFXListView<String> listView;

    private TableColumn<QualifyData, Integer> positionColumn;
    private TableColumn<QualifyData, String> driverNameColumn;
    private TableColumn<QualifyData, String> constructorNameColumn;
    private TableColumn<QualifyData, String> q3ResultsColumn;
    private TableColumn<QualifyData, String> q2ResultsColumn;
    private TableColumn<QualifyData, String> q1ResultsColumn;

    private HashMap<String,List<QualifyData>> qualifyResultsMap;
    private ObservableList<QualifyData> qualifyResultsObservableList;
    private List<QualifyData> qualifyResults;
    private List<String> positions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeContainers();
        createTableViewColumns();
        loadSerializedData();
        fillListViewWithItems();
    }

    protected void initializeContainers() {
        qualifyResultsObservableList = FXCollections.observableArrayList();
        qualifyResults = new ArrayList<>();
        qualifyResultsMap = new HashMap<>();
        positions = new ArrayList<>();
    }

    protected void loadSerializedData() {
        String path = "qualifyResults.ser";
        File serializedFile = new File(path);
        if (serializedFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(path);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                qualifyResultsMap = (HashMap) objectInputStream.readObject();
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
        if (!qualifyResultsMap.isEmpty()) {
            positions.addAll(qualifyResultsMap.keySet());
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
        q3ResultsColumn = new TableColumn<>("Czas Q3");
        q2ResultsColumn = new TableColumn<>("Czas Q2");
        q1ResultsColumn = new TableColumn<>("Czas Q1");

        positionColumn.getStyleClass().add("column-style2");
        driverNameColumn.getStyleClass().add("column-style");
        constructorNameColumn.getStyleClass().add("column-style");
        q3ResultsColumn.getStyleClass().add("column-style");
        q2ResultsColumn.getStyleClass().add("column-style");
        q1ResultsColumn.getStyleClass().add("column-style");

        tableView.getColumns().addAll(positionColumn, driverNameColumn, constructorNameColumn, q3ResultsColumn, q2ResultsColumn, q1ResultsColumn);
    }

    protected void loadDataToTable(String selectedPositon) {
        tableView.getItems().clear();
        getDataForSelectedPosition(selectedPositon);
        qualifyResultsObservableList.addAll(qualifyResults);

        positionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPosition()));
        driverNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDriverName()));
        constructorNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getConstructorName()));
        q1ResultsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQ1Result()));
        q2ResultsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQ2Result()));
        q3ResultsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQ3Result()));

        tableView.setItems(qualifyResultsObservableList);
    }

    protected void getDataForSelectedPosition(String selectedPosition) {
        if (selectedPosition != null) {
            qualifyResults = qualifyResultsMap.get(selectedPosition);
        }
    }

}