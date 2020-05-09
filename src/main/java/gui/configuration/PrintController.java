package gui.configuration;

import gui.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class PrintController implements Initializable {

    @FXML private TilePane graphs = new TilePane();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graphs.getChildren().addAll(Main.getModel().getChartMetrics().getAllCharts());
        graphs.getChildren().forEach(node -> {
            node.scaleXProperty();
            node.scaleYProperty();
            node.scaleZProperty();
        });
    }
}
