package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FavouritesController {
    private Parent parent;
    private Scene scene;
    private Stage stage;

    @FXML
    private void onDriversButtonClick(ActionEvent actionEvent) throws Exception {
        setParent("favouritesFxml/favDriverStandings.fxml");
        setScene(parent,"css/style.css");
        setStage(actionEvent);
        getStage().show();
    }

    @FXML
    private void onConstructorsButtonClick(ActionEvent actionEvent) throws IOException {
        setParent("favouritesFxml/favConstructorStandings.fxml");
        setScene(parent,"css/style.css");
        setStage(actionEvent);
        getStage().show();
    }

    @FXML
    private void onRaceResultsButtonClick(ActionEvent actionEvent) throws IOException {
        setParent("favouritesFxml/favRaceResultsController.fxml");
        setScene(parent,"css/style.css");
        setStage(actionEvent);
        getStage().show();
    }

    @FXML
    private void onQualifyResultsButtonClick(ActionEvent actionEvent) throws IOException {
        setParent("favouritesFxml/favQualifyResultsController.fxml");
        setScene(parent,"css/style.css");
        setStage(actionEvent);
        getStage().show();
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
    private void setScene(Parent parent,String pathToStylesheet){
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
}
