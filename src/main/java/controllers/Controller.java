package controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.olerom.formula.ergast.Ergast;
import ru.olerom.formula.ergast.objects.Schedule;
import ru.olerom.formula.ergast.objects.Season;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Controller {
    private Parent parent;
    private Scene scene;
    private Stage stage;

    private Integer season;
    private Integer roundNumber;
    private Ergast ergast;

    @FXML
    private JFXComboBox<Integer> seasonsComboBox;
    @FXML
    private JFXComboBox<Integer> seasonRaceComboBox;

    protected void onStart(){
        initializeLists();
        setSeason(2018);
        setRoundNumber(21);
        getData();
        createTableViewColumns();
        loadDataToTable();
        fillSeasonsComboBox();
        fillSeasonRaceComboBox();
    }

    protected void onSeasonsHistoryStart(){
        initializeLists();
        setSeason(2018);
        setRoundNumber(21);
        getData();
        createTableViewColumns();
        loadDataToTable();
        fillSeasonsComboBox();
    }

    @FXML
    protected abstract void saveDataToFile(ActionEvent actionEvent) throws IOException;
    protected abstract void createTableViewColumns();
    protected abstract void initializeLists();

    @FXML
    protected void onSeasonsComboBoxClick() {
        setSeason(seasonsComboBox.getValue());
        seasonRaceComboBox.getSelectionModel().clearSelection();
        seasonRaceComboBox.getItems().clear();
        fillSeasonRaceComboBox();
    }

    protected void fillSeasonsComboBox() {
        List<Season> seasonsObjectList = new ArrayList<>();
        List<Integer> seasonsNumberList  = new ArrayList<>();
        ergast = new Ergast(Ergast.NO_SEASON, 80, Ergast.DEFAULT_OFFSET);

        try {
            seasonsObjectList = ergast.getSeasons();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Collections.reverse(seasonsObjectList);
        for (Season season : seasonsObjectList) {
            seasonsNumberList.add(season.getSeason());
        }

        seasonsComboBox.getItems().addAll(seasonsNumberList);
        seasonsComboBox.getSelectionModel().select(season);
    }

    protected void fillSeasonRaceComboBox() {
        List<Schedule> schedulesList = new ArrayList<>();
        List<Integer> grandPrixRoundsList = new ArrayList<>();
        ergast = new Ergast(season, Ergast.DEFAULT_LIMIT, Ergast.DEFAULT_OFFSET);

        try {
            schedulesList = ergast.getSchedules();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (Schedule schedule : schedulesList) {
            grandPrixRoundsList.add(schedule.getRound());
        }

        seasonRaceComboBox.getItems().addAll(grandPrixRoundsList);
        seasonRaceComboBox.getSelectionModel().selectLast();
    }

    @FXML
    protected void reloadTableData() {
        getData();
        loadDataToTable();
    }

    protected abstract void loadDataToTable();

    protected abstract void getData();

    protected String generateListViewKeys(){
        return "Runda " + seasonRaceComboBox.getValue() + " - sezon " + seasonsComboBox.getValue();
    }

    @FXML
    private void onReturnButtonClick(ActionEvent actionEvent) throws IOException {
        setParent("main.fxml");
        setScene(parent,"css/mainMenu.css");
        setStage(actionEvent);
        getStage().show();
    }

    private void setParent(String fxmlTemplate) throws IOException {
        parent = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlTemplate));

    }
    private void setScene(Parent parent, String pathToStylesheet){
        scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getClassLoader().getResource(pathToStylesheet).toExternalForm());
    }

    private void setStage(ActionEvent actionEvent){
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("F1 Stats");
    }

    public Stage getStage() {
        return stage;
    }

    @FXML
    protected void onSeasonRaceComboBoxClick() {
        setRoundNumber(seasonRaceComboBox.getValue());
    }

    protected void setSeason(Integer season) { this.season = season; }

    protected void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    protected Integer getSeason() { return season; }

    protected Integer getRoundNumber() {
        return roundNumber;
    }
}
