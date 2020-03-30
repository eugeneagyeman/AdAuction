package GUI.Segments;

import GUI.Controller;
import GUI.Main;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class SegmentsController extends Controller {

    @FXML private Text fromDateDay = new Text();
    @FXML private Text fromDateMonth = new Text();
    @FXML private Text fromDateYear = new Text();
    @FXML private Text untilDateDay = new Text();
    @FXML private Text untilDateMonth = new Text();
    @FXML private Text untilDateYear = new Text();
    @FXML private TilePane audienceSegmentsGraphs = new TilePane();
    @FXML private CheckBox maleCheckbox = new CheckBox();
    @FXML private CheckBox femaleCheckbox = new CheckBox();

    public void initialiseSegments() {
        accordion.expandedPaneProperty().setValue(audienceSegmentsTab);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseSegments();
    }
}
