package GUI.Overview;

import GUI.Controller;
import GUI.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class OverviewController extends Controller {

    @FXML private Text costPerClickText = new Text();
    @FXML private Text totalImpressionsText = new Text();
    @FXML private Text costPerThousandImpressionsText = new Text();
    @FXML private Text totalClicksText = new Text();
    @FXML private PieChart overviewPieChart = new PieChart();
    @FXML private ListView<String> recommendationsListView = new ListView<>();

    public void initialiseOverview() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        costPerClickText.setText("£" + decimalFormat.format(model.getMetrics().getCostPerClick()));
        totalImpressionsText.setText(String.valueOf(model.getMetrics().getNumOfImpressions()));
        costPerThousandImpressionsText.setText("£" + decimalFormat.format(model.getMetrics().getCostPerThousand()));
        totalClicksText.setText(String.valueOf(model.getMetrics().getNumOfClicks()));
        recommendationsListView.setItems((ObservableList<String>) model.getRecommendations());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseOverview();
    }
}
