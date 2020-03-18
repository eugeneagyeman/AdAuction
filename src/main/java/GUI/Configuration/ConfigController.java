
/**
 * Sample Skeleton for 'overview.fxml' Controller Class
 */

package GUI.Configuration;

import Configuration.Configuration;
import Dashboard.DashboardModel;
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

public class ConfigController implements Initializable {

    private DashboardModel model;

    @FXML private Accordion accordion = new Accordion();
    @FXML private TitledPane overviewTab = new TitledPane();
    @FXML private TitledPane audienceSegmentsTab = new TitledPane();
    @FXML private TitledPane contextTab = new TitledPane();
    @FXML private TitledPane configurationTab = new TitledPane();
    @FXML private TitledPane campaignsTab = new TitledPane();
    @FXML private TextField currentDate = new TextField();
    @FXML private Text usernameText = new Text();
    @FXML private Text userIDText = new Text();
    @FXML private Text companyNameText = new Text();
    @FXML private Text logDateTimeText = new Text();

    public void initialiseContext() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate.setText(dateFormat.format(new Date()));

        overviewTab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    GUI.Main.changeScene("/fxml/Overview.fxml");
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        });
        audienceSegmentsTab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    GUI.Main.changeScene("/fxml/AudienceSegments.fxml");
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        });
        contextTab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    GUI.Main.changeScene("/fxml/Context.fxml");
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        });
        configurationTab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    GUI.Main.changeScene("/fxml/Configuration.fxml");
                } catch (IOException i) {
                    i.printStackTrace();
                }
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

        try {
            model = new Configuration().buildDashboard();
            initialiseContext();
        } catch (IOException i) { i.printStackTrace(); }
    }
}
