package gui;

import configuration.Configuration;
import dashboard.DashboardModel;
import gui.configuration.ConfigController;
import gui.context.ContextController;
import gui.overview.OverviewController;
import gui.segments.SegmentsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import login.Login;

import java.io.IOException;

public class Main extends Application {
    private static Stage mainWindow;
    private static Scene mainScene;
    private static DashboardModel model;
    private static Login login;
    private static OverviewController overviewController;
    private static SegmentsController segmentsController;
    private static ContextController contextController;
    private static ConfigController configController;
    public static Boolean invertedColours = false;

    public static Scene getMainScene() { return mainScene; }

    public static DashboardModel getModel() {
        return model;
    }

    public static void setModel(DashboardModel model) {
        Main.model = model;
    }

    public static Login getLogin() { return login; }

    public static void setLogin(Login login) { Main.login = login; }

    public static Boolean getInvertedColours() { return invertedColours; }

    public static void setInvertedColours(Boolean invertedColours) { Main.invertedColours = invertedColours; }

    public static void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Main.class.getResource(fxml));
        mainWindow.getScene().setRoot(pane);
    }
    public static void changeSceneAndResize(String fxml) throws IOException {
        mainWindow.close();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/Overview.fxml"));
        mainScene = new Scene(loader.load());
        mainWindow.setScene(mainScene);
        mainWindow.show();
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainWindow = stage;
        setModel(new Configuration().buildDashboard());
        setLogin(new Login());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        mainScene = new Scene(loader.load());
        mainWindow.setScene(mainScene);
        mainWindow.show();
    }

    public static void restart() throws IOException {
        mainWindow.close();
        mainWindow = new Stage();
        getLogin().logout();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/Login.fxml"));
        mainScene = new Scene(loader.load());
        mainWindow.setScene(mainScene);
        mainWindow.show();
    }

}
