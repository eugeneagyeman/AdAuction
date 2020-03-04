package GUI;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;

public class MainController {

    @FXML
    private MenuBar MenuBar;

    @FXML
    private Button overviewButton;

    @FXML
    private TitledPane contextAccordian;

    @FXML
    private TitledPane audienceSegmentsAccordian;

    @FXML
    private TitledPane campaignAccordian;

    @FXML
    private Text numericalValueCPC;

    @FXML
    private Text numericalValueOfImpressions;

    @FXML
    private Text numericalValueCPM;

    @FXML
    private Text numericalValueNumOfClicks;

    @FXML
    private PieChart overviewPieChart;

    @FXML
    private ListView<?> recommendationList;


    public void overviewButtonClicked(javafx.event.ActionEvent actionEvent) {

    }
}

