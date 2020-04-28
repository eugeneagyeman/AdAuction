package gui.context;

import gui.Controller;
import gui.Main;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.ResourceBundle;

public class ContextController extends Controller {

    @FXML
    private TextField fromDateDay = new TextField();
    @FXML
    private TextField fromDateMonth = new TextField();
    @FXML
    private TextField fromDateYear = new TextField();
    @FXML
    private TextField toDateDay = new TextField();
    @FXML
    private TextField toDateMonth = new TextField();
    @FXML
    private TextField toDateYear = new TextField();
    @FXML
    private TilePane contextGraphs = new TilePane();
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseContext();
    }
}
