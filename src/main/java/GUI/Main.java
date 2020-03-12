package GUI;

import Configuration.Configuration;
import Dashboard.DashboardModel;
import GUI.Overview.OverviewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage mainWindow;
    private DashboardModel model;

    public static void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(
                Main.class.getResource(fxml));

        mainWindow.getScene().setRoot(pane);
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainWindow = stage;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/Overview.fxml"));

        model = new Configuration().buildDashboard();
        Scene scene = new Scene(loader.load());
        mainWindow.setScene(scene);

        OverviewController controller = loader.getController();
        controller.initModel(model);
        mainWindow.show();
    }
}
