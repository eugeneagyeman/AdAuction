
/**
 * Sample Skeleton for 'overview.fxml' Controller Class
 */

package GUI.Overview;

import Dashboard.DashboardModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class OverviewController implements Initializable {

    private DashboardModel model;

    @FXML private URL url;
    @FXML private ResourceBundle resourceBundle;
    @FXML private TitledPane overviewTab;
    @FXML private TitledPane audienceSegmentsTab;
    @FXML private TitledPane contextTab;
    @FXML private TitledPane ConfigurationTab;
    @FXML private TitledPane campaignsTab;
    @FXML private TextField currentDate;
    @FXML private Text costPerClickText;
    @FXML private Text totalImpressionsText;
    @FXML private Text costPerThousandImpressionsText;
    @FXML private Text totalClicksText;
    @FXML private PieChart overviewPieChart;
    @FXML private ListView recommendationsListView;

    public OverviewController() {
        initialiseOverview();
    }

    public void overviewButtonClicked(javafx.event.ActionEvent actionEvent) throws IOException {
        GUI.Main.changeScene("/fxml/Overview.fxml");
    }

    public void initModel(DashboardModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialised once");
        }
        this.model = model;
    }

    public void initialiseOverview() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        currentDate.setText(dateFormat.format(new Date()));
        costPerClickText.setText(String.valueOf(this.model.getMetrics().getCostPerClick()));
        totalImpressionsText.setText(String.valueOf(this.model.getMetrics().getNumOfImpressions()));
        costPerThousandImpressionsText.setText(String.valueOf(this.model.getMetrics().getCostPerThousand()));
        totalClicksText.setText(String.valueOf(this.model.getMetrics().getNumOfClicks()));
        recommendationsListView = new ListView<String>(model.getRecommendations());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert overviewTab != null : "fx:id=\"overviewButton\" was not injected: check your FXML file 'overview.fxml'.";
        assert contextTab != null : "fx:id=\"contextAccordian\" was not injected: check your FXML file 'overview.fxml'.";
        assert audienceSegmentsTab != null : "fx:id=\"audienceSegmentsAccordian\" was not injected: check your FXML file 'overview.fxml'.";
        assert campaignsTab != null : "fx:id=\"campaignAccordian\" was not injected: check your FXML file 'overview.fxml'.";
        assert costPerClickText != null : "fx:id=\"numericalValueCPC\" was not injected: check your FXML file 'overview.fxml'.";
        assert totalImpressionsText != null : "fx:id=\"numericalValueOfImpressions\" was not injected: check your FXML file 'overview.fxml'.";
        assert costPerThousandImpressionsText != null : "fx:id=\"numericalValueCPM\" was not injected: check your FXML file 'overview.fxml'.";
        assert totalClicksText != null : "fx:id=\"numericalValueNumOfClicks\" was not injected: check your FXML file 'overview.fxml'.";
        assert overviewPieChart != null : "fx:id=\"overviewPieChart\" was not injected: check your FXML file 'overview.fxml'.";
        assert currentDate != null : "fx:id-\"currentDate\" was not injected: check your FXML file 'overview.fxml'.";
        costPerClickText.setText("0");
        totalImpressionsText.setText("0");
        costPerThousandImpressionsText.setText("0");
        totalClicksText.setText("0");
        if (recommendationsListView == null)
            throw new AssertionError("fx:id=\"recommendationList\" was not injected: check your FXML file 'overview.fxml'.");
    }
}
