package GUI.Configuration;

import Dashboard.DashboardModel;
import javafx.fxml.FXML;

import java.awt.*;

public class ConfigController {
    @FXML
    MenuBar menuBar;
    private DashboardModel model;

    public void initModel(DashboardModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialised once");
        }
        this.model = model;

    }
}
