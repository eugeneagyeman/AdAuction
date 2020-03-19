package GUI.Overview;

import Configuration.Configuration;
import Dashboard.DashboardModel;
import GUI.Main;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class OverviewController implements Initializable {

    private DashboardModel model;

    @FXML private Accordion accordion = new Accordion();
    @FXML private TitledPane overviewTab = new TitledPane();
    @FXML private TitledPane audienceSegmentsTab = new TitledPane();
    @FXML private TitledPane contextTab = new TitledPane();
    @FXML private TitledPane configurationTab = new TitledPane();
    @FXML private TitledPane campaignsTab = new TitledPane();
    @FXML private TextField currentDate = new TextField();
    @FXML private Text costPerClickText = new Text();
    @FXML private Text totalImpressionsText = new Text();
    @FXML private Text costPerThousandImpressionsText = new Text();
    @FXML private Text totalClicksText = new Text();
    @FXML private PieChart overviewPieChart = new PieChart();
    @FXML private ListView<String> recommendationsListView = new ListView<>();

    public void initialiseOverview() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        currentDate.setText(dateFormat.format(new Date()));
        costPerClickText.setText("£" + decimalFormat.format(model.getMetrics().getCostPerClick()));
        totalImpressionsText.setText(String.valueOf(model.getMetrics().getNumOfImpressions()));
        costPerThousandImpressionsText.setText("£" + decimalFormat.format(model.getMetrics().getCostPerThousand()));
        totalClicksText.setText(String.valueOf(model.getMetrics().getNumOfClicks()));
        recommendationsListView.setItems(model.getRecommendations());

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
        assert costPerClickText != null : "fx:id=\"costPerClickText\" was not injected: check your FXML file 'overview.fxml'.";
        assert totalImpressionsText != null : "fx:id=\"totalImpressionsText\" was not injected: check your FXML file 'overview.fxml'.";
        assert costPerThousandImpressionsText != null : "fx:id=\"costPerThousandImpressionsText\" was not injected: check your FXML file 'overview.fxml'.";
        assert totalClicksText != null : "fx:id=\"totalClicksText\" was not injected: check your FXML file 'overview.fxml'.";
        assert overviewPieChart != null : "fx:id=\"overviewPieChart\" was not injected: check your FXML file 'overview.fxml'.";
        assert currentDate != null : "fx:id-\"currentDate\" was not injected: check your FXML file 'overview.fxml'.";
        costPerClickText.setText("0");
        totalImpressionsText.setText("0");
        costPerThousandImpressionsText.setText("0");
        totalClicksText.setText("0");
        if (recommendationsListView == null)
            throw new AssertionError("fx:id=\"recommendationList\" was not injected: check your FXML file 'overview.fxml'.");

        model = Main.getModel();
        initialiseOverview();
    }
}
