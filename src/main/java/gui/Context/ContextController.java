package gui.context;

import gui.Controller;
import gui.Main;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ContextController extends Controller {

    @FXML
    private Text fromDateDay = new Text();
    @FXML
    private Text fromDateMonth = new Text();
    @FXML
    private Text fromDateYear = new Text();
    @FXML
    private Text untilDateDay = new Text();
    @FXML
    private Text untilDateMonth = new Text();
    @FXML
    private Text untilDateYear = new Text();
    @FXML
    private TilePane contextGraphs = new TilePane();
    @FXML
    private CheckBox newsBttn;

    @FXML
    private CheckBox shoppingBttn;

    @FXML
    private CheckBox socialMediaBttn;

    @FXML
    private CheckBox hobbiesBttn;

    @FXML
    private CheckBox travelBttn;

    public void initialiseContext() {
        accordion.expandedPaneProperty().setValue(contextTab);
        for (Node checkBox : ((AnchorPane) contextTab.getContent()).getChildren())
            checkBox.setOnMouseClicked(click -> model.getFilter().contextFilter(checkBox.getAccessibleText()));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseContext();
    }
}
