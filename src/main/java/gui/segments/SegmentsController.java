package gui.segments;

import gui.Controller;
import gui.Main;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SegmentsController extends Controller {
    @FXML
    private DatePicker fromDatePicker = new DatePicker();
    @FXML
    private DatePicker untilDatePicker = new DatePicker();
    @FXML
    private VBox audienceSegmentsGraphs = new VBox();
    @FXML
    private TreeView<String> segmentsTreeView;

    public void initialiseSegments() {
        accordion.expandedPaneProperty().setValue(audienceSegmentsTab);
        TreeItem<String> rootItem = new TreeItem<>("Segments");
        TreeItem<String> ageItem = new TreeItem<>("Age");
        TreeItem<String> youngerThan25option = new TreeItem<>("<25");
        TreeItem<String> between25and34option = new TreeItem<>("25-34");
        TreeItem<String> between35and44option = new TreeItem<>("35-44");
        TreeItem<String> between45and54option = new TreeItem<>("45-54");
        TreeItem<String> olderThan54option = new TreeItem<>(">54");
        ageItem.getChildren().addAll(youngerThan25option, between25and34option, between35and44option, between45and54option, olderThan54option);
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

        fromDatePicker.setValue(model.getMetrics().getStartDate());
        untilDatePicker.setValue(model.getMetrics().getEndDate());

        audienceSegmentsGraphs.getChildren().addAll(model.getChartMetrics().getCharts());
        audienceSegmentsGraphs.getChildren().forEach(node -> {
            node.scaleXProperty();
            node.scaleYProperty();
            node.scaleZProperty();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseSegments();
    }
}