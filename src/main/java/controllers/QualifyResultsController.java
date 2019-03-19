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
import ru.olerom.formula.ergast.objects.Qualification;
import serializableClasses.QualifyData;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class QualifyResultsController extends Controller implements Initializable {
    @FXML
    private TableView<QualifyData> tableView;

    private TableColumn<QualifyData, Integer> positionColumn;
    private TableColumn<QualifyData, String> driverNameColumn;
    private TableColumn<QualifyData, String> constructorNameColumn;
    private TableColumn<QualifyData, String> q3ResultsColumn;
    private TableColumn<QualifyData, String> q2ResultsColumn;
    private TableColumn<QualifyData, String> q1ResultsColumn;

    private ObservableList<QualifyData> qualifyResultsObservableList;
    private List<Qualification> qualifyResultsFromAPI;
    private List<QualifyData> qualifyResults;
    private HashMap<String,List<QualifyData>> qualifyResultsMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onStart();
    }

    protected void initializeLists() {
        qualifyResultsObservableList = FXCollections.observableArrayList();
        qualifyResultsFromAPI = new ArrayList<>();
        qualifyResults = new ArrayList<>();
        qualifyResultsMap = new HashMap<>();
    }

    protected void getData() {
        qualifyResults = new ArrayList<>();
        Ergast ergast = new Ergast(getSeason(), 40, Ergast.DEFAULT_OFFSET);

        try {
            qualifyResultsFromAPI = ergast.getQualificationResults(getRoundNumber());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (Qualification qualificationResult: qualifyResultsFromAPI) {
            qualifyResults.add(new QualifyData(qualificationResult.getPosition(), 
                    qualificationResult.getDriver().getGivenName() + " " + qualificationResult.getDriver().getFamilyName(),
                            qualificationResult.getConstructor().getName(), qualificationResult.getQ1(), qualificationResult.getQ2(), qualificationResult.getQ3()));
        }
    }

    protected void loadDataToTable(){
        tableView.getItems().clear();
        qualifyResultsObservableList.addAll(qualifyResults);

        positionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPosition()));
        driverNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDriverName()));
        constructorNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getConstructorName()));
        q1ResultsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQ1Result()));
        q2ResultsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQ2Result()));
        q3ResultsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQ3Result()));

        tableView.setItems(qualifyResultsObservableList);
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

    @FXML
    private void serializeData(ActionEvent actionEvent) {
        try{
            FileOutputStream fileOutputStream= new FileOutputStream("qualifyResults.ser");
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
            qualifyResultsMap.put(generateListViewKeys(),qualifyResults);
            objectOutputStream.writeObject(qualifyResultsMap);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void saveDataToFile(ActionEvent actionEvent) throws IOException {
        String path = "QualifyResults_" + getSeason() + "_round_" + getRoundNumber() + ".txt";
        FileWriter writer = new FileWriter(path);
        for(QualifyData qualifyData: qualifyResults) {
            writer.write(qualifyData.getPosition() + " " + qualifyData.getDriverName() + " " + qualifyData.getQ3Result() + " " + qualifyData.getQ2Result() + " " + qualifyData.getQ1Result());
            writer.write("\n");
        }
        writer.close();
    }

}
