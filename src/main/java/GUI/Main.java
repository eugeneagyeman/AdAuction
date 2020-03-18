package GUI;

import Configuration.Configuration;
import Dashboard.DashboardModel;
import GUI.Configuration.ConfigController;
import GUI.Context.ContextController;
import GUI.Overview.OverviewController;
import GUI.Segments.SegmentsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage mainWindow;
    private static DashboardModel model;
    private static OverviewController overviewController;
    private static SegmentsController segmentsController;
    private static ContextController contextController;
    private static ConfigController configController;

    public static DashboardModel getModel() { return model; }
    public static OverviewController getOverviewController() { return overviewController; }
    public static FXMLLoader getLoader() { return loader; }
    public static void setModel(DashboardModel model) { Main.model = model; }
    public static void setOverviewController(OverviewController overviewController) { Main.overviewController = overviewController; }
    public static void setLoader(FXMLLoader loader) { Main.loader = loader; }


    private static FXMLLoader loader;

    public static void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(
                Main.class.getResource(fxml));
        mainWindow.getScene().setRoot(pane);
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainWindow = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Overview.fxml"));
        Scene scene = new Scene(loader.load());
        mainWindow.setScene(scene);
        mainWindow.show();
    }
}
