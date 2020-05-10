package gui.login;

import gui.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;

public class RegisterController {
    Parent root;
    Stage primaryStage;
    @FXML private TextField userField;
    @FXML private TextField passField;
    @FXML private Button loginButton;
    @FXML private Button adminButton;
    @FXML private Button groupButton;

    public void Register(ActionEvent actionEvent) {
        loginButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String userInput = userField.getText();
                String userPass = passField.getText();
                try {
                    if (Main.getLogin().addUser(userInput,userPass,"user") == null) {
                        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                        root = FXMLLoader.load(this.getClass().getResource("/fxml/InvalidRegister.fxml"));
                        primaryStage = new Stage();
                        primaryStage.setScene(new Scene(root, 280.0D, 250.0D));
                        primaryStage.setTitle("Register Screen");
                        primaryStage.show();
                    }
                } catch (BadPaddingException | IllegalBlockSizeException | IOException e) { e.printStackTrace(); }
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            }
        });
        adminButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String userInput = userField.getText();
                String userPass = passField.getText();
                try {
                    if (Main.getLogin().addUser(userInput,userPass,"admin") == null) {
                        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                        root = FXMLLoader.load(this.getClass().getResource("/fxml/InvalidRegister.fxml"));
                        primaryStage = new Stage();
                        primaryStage.setScene(new Scene(root, 280.0D, 250.0D));
                        primaryStage.setTitle("Register Screen");
                        primaryStage.show();
                    }
                } catch (BadPaddingException | IllegalBlockSizeException | IOException e) { e.printStackTrace(); }
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            }
        });
        groupButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String userInput = userField.getText();
                String userPass = passField.getText();
                try {
                    if (Main.getLogin().addUser(userInput,userPass,"group") == null) {
                        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                        root = FXMLLoader.load(this.getClass().getResource("/fxml/InvalidRegister.fxml"));
                        primaryStage = new Stage();
                        primaryStage.setScene(new Scene(root, 280.0D, 250.0D));
                        primaryStage.setTitle("Register Screen");
                        primaryStage.show();
                    }
                } catch (BadPaddingException | IllegalBlockSizeException | IOException e) { e.printStackTrace(); }
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            }
        });
    }

}
