package gui.overview;

import gui.Controller;
import gui.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class OverviewController extends Controller {

    @FXML private Text costPerClickText = new Text();
    @FXML private Text totalImpressionsText = new Text();
    @FXML private Text costPerThousandImpressionsText = new Text();
    @FXML private Text totalClicksText = new Text();
    @FXML private PieChart overviewPieChart = new PieChart();
    @FXML private ListView<String> recommendationsListView = new ListView<>();
    @FXML private VBox demographicsBox;

    public void initialiseOverview() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        costPerClickText.setText("£" + decimalFormat.format(model.getMetrics().getCostPerClick()));
        totalImpressionsText.setText(String.valueOf(model.getMetrics().getNumOfImpressions()));
        costPerThousandImpressionsText.setText("£" + decimalFormat.format(model.getMetrics().getCostPerThousand()));
        totalClicksText.setText(String.valueOf(model.getMetrics().getNumOfClicks()));
        recommendationsListView.setItems((ObservableList<String>) model.getRecommendations());
        overviewPieChart = model.getChartMetrics().buildContextDistribution();
        demographicsBox.getChildren().add(overviewPieChart);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseOverview();
    }
}
