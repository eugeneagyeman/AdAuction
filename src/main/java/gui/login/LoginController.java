package gui.login;

import java.io.IOException;

import POJOs.Privilege;
import gui.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {
    Parent root;
    Stage primaryStage;
    @FXML private Label loginLabel;
    @FXML private TextField userField;
    @FXML private TextField passField;
    @FXML private Button registerButton;
    @FXML private Button loginButton;
    @FXML private Button adminButton;
    @FXML private Button groupButton;

    public void Login(ActionEvent actionEvent) {
        loginButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try { userLogin(Privilege.USER); } catch (IOException e) { e.printStackTrace(); }
            }
        });
        adminButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try { userLogin(Privilege.ADMIN); } catch (IOException e) { e.printStackTrace(); }
            }
        });
        groupButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try { userLogin(Privilege.GROUP); } catch (IOException e) { e.printStackTrace(); }
            }
        });
    }

    public void Register(ActionEvent actionEvent) {
        registerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    root = FXMLLoader.load(this.getClass().getResource("/fxml/Register.fxml"));
                    primaryStage = new Stage();
                    primaryStage.setScene(new Scene(root, 450.0D, 300.0D));
                    primaryStage.setTitle("Register Screen");
                    primaryStage.show();
                } catch (IOException e) { e.printStackTrace(); }
            }
        });
    }

    public void userLogin(Privilege privilege) throws IOException {
        String userInput = userField.getText();
        String userPass = passField.getText();

        if (Main.getLogin().login(userInput,userPass,privilege) != null) {
            this.loginLabel.setText("Log in successful");
            Main.changeScene("/fxml/FileChooser.fxml");
        } else {
            root = FXMLLoader.load(this.getClass().getResource("/fxml/InvalidLogin.fxml"));
            primaryStage = new Stage();
            primaryStage.setTitle("Invalid Credentials Screen");
            primaryStage.setScene(new Scene(root, 300.0D, 150.0D));
            primaryStage.show();
            this.loginLabel.setText("Please provide correct login details");
        }
    }
}
