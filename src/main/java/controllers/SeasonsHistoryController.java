package controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import ru.olerom.formula.ergast.Ergast;
import ru.olerom.formula.ergast.objects.Schedule;
import serializableClasses.GPInformationData;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SeasonsHistoryController extends Controller implements Initializable {
    @FXML
    private TableView<GPInformationData> tableView;

    private TableColumn<GPInformationData, Integer> roundColumn;
    private TableColumn<GPInformationData, String> countryColumn;
    private TableColumn<GPInformationData, String> localityColumn;
    private TableColumn<GPInformationData, String> circuitNameColumn;
    private TableColumn<GPInformationData, String> dateColumn;
    private TableColumn<GPInformationData, String> timeColumn;

    private ObservableList<GPInformationData> gpInformationsObservableList;
    private List<Schedule> gpInformationsFromAPI;
    private List<GPInformationData> gpInformationResults;

    @FXML
    private JFXComboBox<Integer> seasonsComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onSeasonsHistoryStart();
    }

    protected void initializeLists() {
        gpInformationsObservableList = FXCollections.observableArrayList();
        gpInformationsFromAPI = new ArrayList<>();
        gpInformationResults = new ArrayList<>();
    }

    protected void getData() {
        gpInformationResults = new ArrayList<>();
        Ergast ergast = new Ergast(getSeason(), 40, Ergast.DEFAULT_OFFSET);

        try {
            gpInformationsFromAPI = ergast.getSchedules();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (Schedule schedule: gpInformationsFromAPI) {
            gpInformationResults.add(new GPInformationData(schedule.getRound(),
                    schedule.getCircuit().getLocation().getCountry(), schedule.getCircuit().getLocation().getLocality(),
                    schedule.getCircuit().getCircuitName(), schedule.getDate(), schedule.getTime()));
        }
    }

    protected void loadDataToTable(){
        tableView.getItems().clear();
        gpInformationsObservableList.addAll(gpInformationResults);

        roundColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getRound()));
        countryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
        localityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocality()));
        circuitNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCircuitName()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));

        tableView.setItems(gpInformationsObservableList);
    }

    protected void createTableViewColumns() {
        tableView.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
            @Override
            public Boolean call(TableView.ResizeFeatures p) {
                return true;
            }
        });

        roundColumn = new TableColumn<>("Runda");
        countryColumn = new TableColumn<>("Kraj");
        localityColumn = new TableColumn<>("Miasto");
        circuitNameColumn = new TableColumn<>("Nazwa toru");
        dateColumn = new TableColumn<>("Dzień");
        timeColumn = new TableColumn<>("Godzina");

        roundColumn.getStyleClass().add("column-style2");
        countryColumn.getStyleClass().add("column-style");
        localityColumn.getStyleClass().add("column-style");
        circuitNameColumn.getStyleClass().add("column-style");
        dateColumn.getStyleClass().add("column-style");
        timeColumn.getStyleClass().add("column-style");

        tableView.getColumns().addAll(roundColumn, countryColumn, localityColumn, circuitNameColumn, dateColumn, timeColumn);
    }

    @FXML
    protected void saveDataToFile(ActionEvent actionEvent) throws IOException {
        String path = "Season_" + getSeason() + "_GPInformations" + ".txt";
        FileWriter writer = new FileWriter(path);
        for(GPInformationData gpData: gpInformationResults) {
            writer.write(gpData.getRound() + " " + gpData.getCountry() + " " + gpData.getCircuitName() + " " + gpData.getLocality() + " " + gpData.getDate() + " " + gpData.getTime());
            writer.write("\n");
        }
        writer.close();
    }
}