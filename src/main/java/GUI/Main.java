package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Stage window = stage;

        String resourcepath = "fxml/overview.fxml";
        URL location = getClass().getResource(resourcepath);
        FXMLLoader fxmlLoader = new FXMLLoader(location);

        VBox vBox = fxmlLoader.load();

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();


    }
}
