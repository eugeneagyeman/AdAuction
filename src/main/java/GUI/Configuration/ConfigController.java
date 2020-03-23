package GUI.Configuration;

import Configuration.Configuration;
import Dashboard.DashboardModel;
import GUI.Controller;
import GUI.Main;
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

public class ConfigController extends Controller {

    @FXML private Text usernameText = new Text();
    @FXML private Text userIDText = new Text();
    @FXML private Text companyNameText = new Text();
    @FXML private Text logDateTimeText = new Text();
    @FXML private Button saveAsPDFButton = new Button();
    @FXML private Button printButton = new Button();

    public void initialiseConfiguration() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseConfiguration();
    }
}
