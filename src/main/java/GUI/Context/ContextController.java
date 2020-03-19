package GUI.Context;

import Dashboard.DashboardModel;
import GUI.Controller;
import GUI.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ContextController extends Controller {

    @FXML private Text fromDateDay = new Text();
    @FXML private Text fromDateMonth = new Text();
    @FXML private Text fromDateYear = new Text();
    @FXML private Text untilDateDay = new Text();
    @FXML private Text untilDateMonth = new Text();
    @FXML private Text untilDateYear = new Text();
    @FXML private TilePane contextGraphs = new TilePane();

    public void initialiseContext() {
        accordion.expandedPaneProperty().setValue(contextTab);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseContext();
    }
}
