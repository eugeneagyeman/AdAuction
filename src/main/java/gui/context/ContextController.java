package gui.context;

import gui.Controller;
import gui.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ContextController extends Controller {

    @FXML
    private DatePicker fromDatePicker = new DatePicker();
    @FXML
    private DatePicker untilDatePicker = new DatePicker();
    @FXML
    private VBox contextGraphs = new VBox();
    @FXML
    private TreeView<String> contextTreeView;

    public void initialiseContext() {
        accordion.expandedPaneProperty().setValue(contextTab);
        TreeItem<String> rootItem = new TreeItem<>("Context");
        TreeItem<String> newsItem = new TreeItem<>("News");
        TreeItem<String> shoppingItem = new TreeItem<>("Shopping");
        TreeItem<String> socialMediaItem = new TreeItem<>("Social Media");
        TreeItem<String> hobbiesItem = new TreeItem<>("Hobbies");
        TreeItem<String> travelItem = new TreeItem<>("Travel");
        rootItem.getChildren().addAll(newsItem, shoppingItem, socialMediaItem, hobbiesItem, travelItem);
        contextTreeView.setRoot(rootItem);
        contextTreeView.setShowRoot(false);

        fromDatePicker.setValue(model.getMetrics().getStartDate());
        untilDatePicker.setValue(model.getMetrics().getEndDate());

        contextGraphs.getChildren().addAll(model.getChartMetrics().getContextCharts());
        contextGraphs.getChildren().forEach(node -> {
            node.scaleXProperty();
            node.scaleYProperty();
            node.scaleZProperty();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseContext();
    }
}
