package gui.login;

import java.io.IOException;

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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class LoginController {
    Parent root;
    Stage primaryStage;
    @FXML private Label loginLabel;
    @FXML private TextField userField;
    @FXML private TextField passField;
    @FXML private Button registerButton;
    @FXML protected Button loginButton;
    @FXML private Button adminButton;
    @FXML private Button groupButton;

    public void Login() {
        loginButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try { userLogin();
                } catch (IOException | BadPaddingException | IllegalBlockSizeException e) { e.printStackTrace(); } }
        });
        adminButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    adminLogin();
                } catch (IOException | BadPaddingException | IllegalBlockSizeException e) { e.printStackTrace(); }
            }
        });
        groupButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    groupLogin();
                } catch (IOException | BadPaddingException | IllegalBlockSizeException e) { e.printStackTrace(); }
            }
        });
    }

    public void Register() {
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

    public void userLogin() throws IOException, BadPaddingException, IllegalBlockSizeException {
        String userInput = userField.getText();
        String userPass = passField.getText();
        if (Main.getLogin().login(userInput,userPass,"user") != null) {
            this.loginLabel.setText("Log in successful");
            Main.changeScene("/fxml/FileChooser.fxml");
        } else {
            root = FXMLLoader.load(this.getClass().getResource("/fxml/InvalidLogin.fxml"));
            primaryStage = new Stage();
            primaryStage.setTitle("Invalid credentials screen");
            primaryStage.setScene(new Scene(root, 300.0D, 150.0D));
            primaryStage.show();
            this.loginLabel.setText("Please provide correct login details");
        }
    }
    public void adminLogin() throws IOException, BadPaddingException, IllegalBlockSizeException {
        String userInput = userField.getText();
        String userPass = passField.getText();
        if (Main.getLogin().login(userInput,userPass,"admin") != null) {
            this.loginLabel.setText("Log in successful");
            Main.changeScene("/fxml/FileChooser.fxml");
        } else {
            root = FXMLLoader.load(this.getClass().getResource("/fxml/InvalidLogin.fxml"));
            primaryStage = new Stage();
            primaryStage.setTitle("Invalid credentials screen");
            primaryStage.setScene(new Scene(root, 300.0D, 150.0D));
            primaryStage.show();
            this.loginLabel.setText("Please provide correct login details");
        }
    }
    public void groupLogin() throws IOException, BadPaddingException, IllegalBlockSizeException {
        String userInput = userField.getText();
        String userPass = passField.getText();
        if (Main.getLogin().login(userInput,userPass,"group") != null) {
            this.loginLabel.setText("Log in successful");
            Main.changeScene("/fxml/FileChooser.fxml");
        } else {
            root = FXMLLoader.load(this.getClass().getResource("/fxml/InvalidLogin.fxml"));
            primaryStage = new Stage();
            primaryStage.setTitle("Invalid credentials screen");
            primaryStage.setScene(new Scene(root, 300.0D, 150.0D));
            primaryStage.show();
            this.loginLabel.setText("Please provide correct login details");
        }
    }


}
