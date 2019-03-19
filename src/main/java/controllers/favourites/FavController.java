package controllers.favourites;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class FavController {
    private Parent parent;
    private Scene scene;
    private Stage stage;

    protected void onStart(){
        initializeContainers();
        createTableViewColumns();
        loadSerializedData();
        fillListViewWithItems();
    }

    protected abstract void initializeContainers();
    protected abstract void createTableViewColumns();
    protected abstract void loadSerializedData();
    protected abstract void fillListViewWithItems();
    protected abstract void passDataIntoTableView(MouseEvent mouseEvent);
    protected abstract void loadDataToTable(String selectedPositon);
    protected abstract void getDataForSelectedPosition(String selectedPosition);

    @FXML
    private void onReturnButtonClick(ActionEvent actionEvent) throws IOException {
        setParent("favourities.fxml");
        setScene(parent, "css/style.css");
        setStage(actionEvent);
        getStage().show();
    }

    private void setParent(String fxmlTemplate) throws IOException {
        parent = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlTemplate));
    }

    private void setScene(Parent parent, String pathToStylesheet) {
        scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getClassLoader().getResource(pathToStylesheet).toExternalForm());
    }

    private void setStage(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("F1 Stats");
    }

    private Stage getStage() {
        return stage;
    }
}
