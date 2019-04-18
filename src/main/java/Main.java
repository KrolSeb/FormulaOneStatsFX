import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        setUtf8Encoding();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/mainMenu.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("F1 Stats");

        primaryStage.show();
    }

    private void setUtf8Encoding() throws NoSuchFieldException, IllegalAccessException {
        System.setProperty("file.encoding","UTF-8");
        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null,null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
