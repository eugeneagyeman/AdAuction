package GUI.Segments;

import Configuration.Configuration;
import Dashboard.DashboardModel;
import GUI.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class SegmentsController implements Initializable {

    private DashboardModel model;

    @FXML private Accordion accordion = new Accordion();
    @FXML private TitledPane overviewTab = new TitledPane();
    @FXML private TitledPane audienceSegmentsTab = new TitledPane();
    @FXML private TitledPane contextTab = new TitledPane();
    @FXML private TitledPane configurationTab = new TitledPane();
    @FXML private TitledPane campaignsTab = new TitledPane();
    @FXML private TextField currentDate = new TextField();
    @FXML private Text fromDateDay = new Text();
    @FXML private Text fromDateMonth = new Text();
    @FXML private Text fromDateYear = new Text();
    @FXML private Text untilDateDay = new Text();
    @FXML private Text untilDateMonth = new Text();
    @FXML private Text untilDateYear = new Text();
    @FXML private TilePane audienceSegmentsGraphs = new TilePane();

    public void initialiseSegments() {
        accordion.expandedPaneProperty().setValue(audienceSegmentsTab);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate.setText(dateFormat.format(new Date()));

        overviewTab.setOnMouseClicked(event -> {
            try {
                Main.changeScene("/fxml/Overview.fxml");
            } catch (IOException i) {
                i.printStackTrace();
            }
        });
        audienceSegmentsTab.setOnMouseClicked(event -> {
            try {
                Main.changeScene("/fxml/AudienceSegments.fxml");
            } catch (IOException i) {
                i.printStackTrace();
            }
        });
        contextTab.setOnMouseClicked(event -> {
            try {
                Main.changeScene("/fxml/Context.fxml");
            } catch (IOException i) {
                i.printStackTrace();
            }
        });
        configurationTab.setOnMouseClicked(event -> {
            try {
                Main.changeScene("/fxml/Configuration.fxml");
            } catch (IOException i) {
                i.printStackTrace();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert overviewTab != null : "fx:id=\"overviewTab\" was not injected: check your FXML file 'overview.fxml'.";
        assert contextTab != null : "fx:id=\"contextTab\" was not injected: check your FXML file 'overview.fxml'.";
        assert audienceSegmentsTab != null : "fx:id=\"audienceSegmentsTab\" was not injected: check your FXML file 'overview.fxml'.";
        assert campaignsTab != null : "fx:id=\"campaignsTab\" was not injected: check your FXML file 'overview.fxml'.";
        assert configurationTab != null : "fx:id=\"configurationsTab\" was not injected: check your FXML file 'overview.fxml'.";
        assert currentDate != null : "fx:id-\"currentDate\" was not injected: check your FXML file 'overview.fxml'.";

        model = Main.getModel();
        initialiseSegments();
    }
}
