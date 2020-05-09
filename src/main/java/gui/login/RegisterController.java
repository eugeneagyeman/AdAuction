package gui.login;

import POJOs.Privilege;
import gui.Main;
import javafx.concurrent.Task;
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

import java.io.IOException;
import java.util.EventObject;

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
                attemptLogin(actionEvent);
            }


        });
        adminButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                attemptLogin(actionEvent);
            }
        });
        groupButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                attemptLogin(actionEvent);
            }
        });
    }
    private void attemptLogin(EventObject actionEvent) {
        String userInput = userField.getText();
        String userPass = passField.getText();
        if (Main.getLogin().addUser(userInput,userPass, Privilege.valueOf("user")) == null) {
            try {
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                root = FXMLLoader.load(this.getClass().getResource("/fxml/InvalidRegister.fxml"));
                primaryStage = new Stage();
                primaryStage.setScene(new Scene(root, 300.0D, 150.0D));
                primaryStage.setTitle("Register Screen");
                primaryStage.show();
            } catch (IOException e) { e.printStackTrace(); }
        }
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

}
