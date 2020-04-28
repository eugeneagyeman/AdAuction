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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage mainWindow;
    private static DashboardModel model;
    private static OverviewController overviewController;
    private static SegmentsController segmentsController;
    private static ContextController contextController;
    private static ConfigController configController;

    public static DashboardModel getModel() {
        return model;
    }

    public static void setModel(DashboardModel model) {
        Main.model = model;
    }

    public static OverviewController getOverviewController() {
        return overviewController;
    }

    public static void setOverviewController(OverviewController overviewController) {
        Main.overviewController = overviewController;
    }

    public static FXMLLoader getLoader() {
        return loader;
    }

    public static void setLoader(FXMLLoader loader) {
        Main.loader = loader;
    }

    private static FXMLLoader loader;

    public static void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(
                Main.class.getResource(fxml));
        mainWindow.getScene().setRoot(pane);
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainWindow = stage;
        model = new Configuration().buildDashboard();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Overview.fxml"));
        Scene scene = new Scene(loader.load());
        mainWindow.setScene(scene);
        mainWindow.show();

//        BarChart barChart = model.getChartMetrics().getHistogram();
//        LineChart numOfImpressions = model.getChartMetrics().getImpressionTimeChart();
//        LineChart numOfClicks = model.getChartMetrics().getNumOfClickChart();
//        StackedBarChart numOfConversion = model.getChartMetrics().getConversionChart();
//        PieChart pieChart = model.getChartMetrics().getContextChart();

//        Stage chartPreviewWindow = new Stage();
//        VBox vBox = new VBox();
//        vBox.getChildren().addAll(barChart, numOfImpressions, numOfClicks, numOfConversion, pieChart);

//        StackPane root = new StackPane();
//        root.getChildren().add(vBox);

//        Scene previewScene = new Scene(root, 800, 450);
//
//        chartPreviewWindow.setTitle("Test Histogram");
//        mainWindow.setScene(previewScene);
        mainWindow.show();

    }
}
