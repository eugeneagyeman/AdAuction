package GUI.Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML
    private Label loginLabel;

    @FXML
    private TextField userField;

    @FXML
    private TextField passField;

    public void Login(ActionEvent actionEvent) throws IOException {
        if (userField.getText().equals("user") && passField.getText().equals("1234")) {
            loginLabel.setText("Log in successful");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/FileChooser.fxml"));
            Stage fileChooserStage = new Stage();
            fileChooserStage.setTitle("Choose file screen");
            fileChooserStage.setScene(new Scene(root, 1280, 720));
            fileChooserStage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } else if (!userField.getText().equals("user") || !passField.getText().equals("1234")){
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/InvalidLogin.fxml"));
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Invalid credentials screen");
            primaryStage.setScene(new Scene(root, 300, 150));
            primaryStage.show();
            loginLabel.setText("Please provide correct login details");
        }
    }
}
