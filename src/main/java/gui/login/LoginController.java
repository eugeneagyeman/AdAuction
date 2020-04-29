package gui.login;

import java.io.IOException;

import gui.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Label loginLabel;
    @FXML
    private TextField userField;
    @FXML
    private TextField passField;

    public void Login(ActionEvent actionEvent) throws IOException {
        Parent root;
        Stage primaryStage;
        if (this.userField.getText().equals("user") && this.passField.getText().equals("1234")) {
            this.loginLabel.setText("Log in successful");
            Main.changeScene("/fxml/FileChooser.fxml");
        } else if (!this.userField.getText().equals("user") || !this.passField.getText().equals("1234")) {
            root = (Parent)FXMLLoader.load(this.getClass().getResource("/fxml/InvalidLogin.fxml"));
            primaryStage = new Stage();
            primaryStage.setTitle("Invalid credentials screen");
            primaryStage.setScene(new Scene(root, 300.0D, 150.0D));
            primaryStage.show();
            this.loginLabel.setText("Please provide correct login details");
        }

    }
}
