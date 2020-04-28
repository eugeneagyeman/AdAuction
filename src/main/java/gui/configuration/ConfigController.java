package gui.configuration;

import gui.Controller;
import gui.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
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
