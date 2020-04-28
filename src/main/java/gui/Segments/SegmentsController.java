package gui.segments;

import gui.Controller;
import gui.Main;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class SegmentsController extends Controller {

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
    private TilePane audienceSegmentsGraphs = new TilePane();
    @FXML
    private TreeView<String> segmentsTreeView;

    public void initialiseSegments() {
        accordion.expandedPaneProperty().setValue(audienceSegmentsTab);
        TreeItem<String> rootItem = new TreeItem<>("Segments");

        TreeItem<?> ageItem = new TreeItem<Object>("Age");
        for (Object range : model.getCurrentCampaign().getMetrics().getAgeRanges()) {
            ageItem.getChildren().add(new TreeItem(range));
        }

        TreeItem<String> genderItem = new TreeItem<>("Gender");
        TreeItem<String> maleGenderItem = new TreeItem<>("Male");
        TreeItem<String> femaleGenderItem = new TreeItem<>("Female");

        genderItem.getChildren().addAll(maleGenderItem, femaleGenderItem);

        TreeItem<String> incomeItem = new TreeItem<>("Income");
        TreeItem<String> lowItem = new TreeItem<>("Low");
        TreeItem<String> mediumItem = new TreeItem<>("Medium");
        TreeItem<String> highItem = new TreeItem<>("High");

        incomeItem.getChildren().addAll(lowItem, mediumItem, highItem);

        rootItem.getChildren().addAll((TreeItem<String>) ageItem, genderItem, incomeItem);
        segmentsTreeView.setRoot(rootItem);
        segmentsTreeView.setShowRoot(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseSegments();


    }
}
