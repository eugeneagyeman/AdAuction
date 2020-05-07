package gui.login;

import javafx.fxml.FXML;
import javafx.scene.Node;

import java.awt.*;

public class InvalidRegister {

    @FXML
    private Button closeButton;

    public void closeWindow(javafx.event.ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

}
