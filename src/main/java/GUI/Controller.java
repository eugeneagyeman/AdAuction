package GUI;

import Dashboard.DashboardModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    protected DashboardModel model;

    @FXML protected Accordion accordion = new Accordion();
    @FXML protected TitledPane overviewTab = new TitledPane();
    @FXML protected TitledPane audienceSegmentsTab = new TitledPane();
    @FXML protected TitledPane contextTab = new TitledPane();
    @FXML protected TitledPane configurationTab = new TitledPane();
    @FXML protected TitledPane campaignsTab = new TitledPane();
    @FXML protected TextField currentDate = new TextField();

    public void initialiseController() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
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
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}