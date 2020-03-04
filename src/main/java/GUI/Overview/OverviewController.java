
/**
 * Sample Skeleton for 'overview.fxml' Controller Class
 */

package GUI.Overview;

import Dashboard.DashboardModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OverviewController implements Initializable {

    private DashboardModel model;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML // fx:id="MenuBar"
    private MenuBar MenuBar; // Value injected by FXMLLoader
    @FXML // fx:id="overviewButton"
    private Button overviewButton; // Value injected by FXMLLoader
    @FXML // fx:id="contextAccordian"
    private TitledPane contextAccordian; // Value injected by FXMLLoader
    @FXML // fx:id="audienceSegmentsAccordian"
    private TitledPane audienceSegmentsAccordian; // Value injected by FXMLLoader
    @FXML // fx:id="campaignAccordian"
    private TitledPane campaignAccordian; // Value injected by FXMLLoader
    @FXML // fx:id="numericalValueCPC"
    private Text numericalValueCPC; // Value injected by FXMLLoader
    @FXML // fx:id="numericalValueOfImpressions"
    private Text numericalValueOfImpressions; // Value injected by FXMLLoader
    @FXML // fx:id="numericalValueCPM"
    private Text numericalValueCPM; // Value injected by FXMLLoader
    @FXML // fx:id="numericalValueNumOfClicks"
    private Text numericalValueNumOfClicks; // Value injected by FXMLLoader
    @FXML // fx:id="overviewPieChart"
    private PieChart overviewPieChart; // Value injected by FXMLLoader
    @FXML // fx:id="recommendationList"
    private ListView<?> recommendationList; // Value injected by FXMLLoader

    public OverviewController() {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete

    public void overviewButtonClicked(javafx.event.ActionEvent actionEvent) throws IOException {
        GUI.Main.changeScene("");

    }

    public void initModel(DashboardModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialised once");
        }
        this.model = model;
        initialiseOverview();

    }

    public void initialiseOverview() {
        numericalValueCPC.setText(String.valueOf(this.model.getMetrics().getCostPerClick()));
        numericalValueOfImpressions.setText(String.valueOf(this.model.getMetrics().getNumOfImpressions()));
        numericalValueNumOfClicks.setText(String.valueOf(this.model.getMetrics().getNumOfClicks()));
        numericalValueCPM.setText(String.valueOf(this.model.getMetrics().getCostPerThousand()));
        recommendationList.setItems(model.getRecommendations());


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert MenuBar != null : "fx:id=\"MenuBar\" was not injected: check your FXML file 'overview.fxml'.";
        assert overviewButton != null : "fx:id=\"overviewButton\" was not injected: check your FXML file 'overview.fxml'.";
        assert contextAccordian != null : "fx:id=\"contextAccordian\" was not injected: check your FXML file 'overview.fxml'.";
        assert audienceSegmentsAccordian != null : "fx:id=\"audienceSegmentsAccordian\" was not injected: check your FXML file 'overview.fxml'.";
        assert campaignAccordian != null : "fx:id=\"campaignAccordian\" was not injected: check your FXML file 'overview.fxml'.";
        assert numericalValueCPC != null : "fx:id=\"numericalValueCPC\" was not injected: check your FXML file 'overview.fxml'.";
        assert numericalValueOfImpressions != null : "fx:id=\"numericalValueOfImpressions\" was not injected: check your FXML file 'overview.fxml'.";
        assert numericalValueCPM != null : "fx:id=\"numericalValueCPM\" was not injected: check your FXML file 'overview.fxml'.";
        assert numericalValueNumOfClicks != null : "fx:id=\"numericalValueNumOfClicks\" was not injected: check your FXML file 'overview.fxml'.";
        assert overviewPieChart != null : "fx:id=\"overviewPieChart\" was not injected: check your FXML file 'overview.fxml'.";
        if (recommendationList == null)
            throw new AssertionError("fx:id=\"recommendationList\" was not injected: check your FXML file 'overview.fxml'.");

        numericalValueCPC.setText("0");
        numericalValueCPM.setText("0");
        numericalValueNumOfClicks.setText("0");
        numericalValueOfImpressions.setText("0");
    }
}
