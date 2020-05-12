package gui.segments;

import POJOs.Records;
import com.google.common.collect.Multimap;
import gui.Controller;
import gui.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SegmentsController extends Controller {
    Parent root;
    Stage primaryStage;
    @FXML private DatePicker fromDatePicker = new DatePicker();
    @FXML private DatePicker untilDatePicker = new DatePicker();
    @FXML private VBox audienceSegmentsGraphs = new VBox();
    @FXML private TreeView<String> segmentsTreeView;
    @FXML private Button okButton;

    public void initialiseSegments() {
        accordion.expandedPaneProperty().setValue(audienceSegmentsTab);
        TreeItem<String> rootItem = new TreeItem<>("Segments");
        TreeItem<String> ageItem = new TreeItem<>("Age");
        CheckBoxTreeItem<String> youngerThan25option = new CheckBoxTreeItem<>("<25");
        CheckBoxTreeItem<String> between25and34option = new CheckBoxTreeItem<>("25-34");
        CheckBoxTreeItem<String> between35and44option = new CheckBoxTreeItem<>("35-44");
        CheckBoxTreeItem<String> between45and54option = new CheckBoxTreeItem<>("45-54");
        CheckBoxTreeItem<String> olderThan54option = new CheckBoxTreeItem<>(">54");
        ageItem.getChildren().addAll(youngerThan25option, between25and34option, between35and44option, between45and54option, olderThan54option);
        TreeItem<String> genderItem = new TreeItem<>("Gender");
        CheckBoxTreeItem<String> maleGenderItem = new CheckBoxTreeItem<>("Male");
        CheckBoxTreeItem<String> femaleGenderItem = new CheckBoxTreeItem<>("Female");
        genderItem.getChildren().addAll(maleGenderItem, femaleGenderItem);
        TreeItem<String> incomeItem = new TreeItem<>("Income");
        CheckBoxTreeItem<String> lowItem = new CheckBoxTreeItem<>("Low");
        CheckBoxTreeItem<String> mediumItem = new CheckBoxTreeItem<>("Medium");
        CheckBoxTreeItem<String> highItem = new CheckBoxTreeItem<>("High");
        incomeItem.getChildren().addAll(lowItem, mediumItem, highItem);
        rootItem.getChildren().addAll(ageItem, genderItem, incomeItem);
        segmentsTreeView.setRoot(rootItem);
        segmentsTreeView.setShowRoot(false);

        segmentsTreeView.getSelectionModel().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {

            TreeItem<String> selectedItem = (TreeItem<String>) newValue;
            String parent = selectedItem.getParent().getValue();
            String category = selectedItem.getValue();
            String selectedFilter = parent + "," + category;
            System.out.println("Selected Filter : " + selectedFilter);

            try {
                Multimap filteredMap = model.getFilterTree().filter(selectedFilter);
                Boolean hasUpdate = model.updateCharts(filteredMap);
                if(hasUpdate) rebuildCharts();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        fromDatePicker.setValue(model.getMetrics().getStartDate());
        untilDatePicker.setValue(model.getMetrics().getEndDate());
        addCharts();
    }

    private void addCharts() {
        audienceSegmentsGraphs.getChildren().addAll(model.getChartMetrics().getSegmentCharts());
        audienceSegmentsGraphs.getChildren().forEach(node -> {
            node.scaleXProperty();
            node.scaleYProperty();
            node.scaleZProperty();
        });
    }

    private void rebuildCharts() {
        audienceSegmentsGraphs.getChildren().clear();
        addCharts();
    }

    public void invalidDates(ActionEvent actionEvent) {
        okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (false) {
                    // filter by dates
                }
                else {
                    // open error window
                    try {
                        root = FXMLLoader.load(this.getClass().getResource("/fxml/InvalidDates.fxml"));
                        primaryStage = new Stage();
                        primaryStage.setTitle("Error");
                        primaryStage.setScene(new Scene(root, 325.0D, 160.0D));
                        primaryStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseSegments();
    }
}