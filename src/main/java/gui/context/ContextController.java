package gui.context;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContextController extends Controller {
    Parent root;
    Stage primaryStage;
    @FXML private DatePicker fromDatePicker = new DatePicker();
    @FXML private DatePicker untilDatePicker = new DatePicker();
    @FXML private VBox contextGraphs = new VBox();
    @FXML private TreeView<String> contextTreeView;
    @FXML private Button okButton;

    public void initialiseContext() {
        accordion.expandedPaneProperty().setValue(contextTab);
        TreeItem<String> rootItem = new TreeItem<>("Context");
        CheckBoxTreeItem<String> newsItem = new CheckBoxTreeItem<>("News");
        CheckBoxTreeItem<String> shoppingItem = new CheckBoxTreeItem<>("Shopping");
        CheckBoxTreeItem<String> socialMediaItem = new CheckBoxTreeItem<>("Social Media");
        CheckBoxTreeItem<String> hobbiesItem = new CheckBoxTreeItem<>("Hobbies");
        CheckBoxTreeItem<String> travelItem = new CheckBoxTreeItem<>("Travel");
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

        contextTreeView.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue,
                                Object newValue) {

                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                System.out.println("Selected Text : " + selectedItem.getValue());
                // do what ever you want 
            }

        });
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

    private String getSelectedDates() {
        TreeItem<String> selectedItem = contextTreeView.getSelectionModel().getSelectedItem();
        return selectedItem == null ? null : selectedItem.getValue();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseContext();
    }
}
