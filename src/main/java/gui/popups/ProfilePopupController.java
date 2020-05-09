package gui.popups;

import gui.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfilePopupController implements Initializable {

    @FXML Text usernameText = new Text();
    @FXML Text userIDText = new Text();
    @FXML Button logoutButton = new Button();
    @FXML Button closeButton = new Button();

    public void logout() throws IOException {
        Main.restart();
    }

    public void closeWindow(javafx.event.ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameText.setText("Username: " + Main.getLogin().getCurrentUser().getUsername());
        userIDText.setText("User ID: " + Main.getLogin().getUserID());
    }
}
