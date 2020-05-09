package gui.popups;

import gui.Controller;
import gui.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsPopupController implements Initializable {

    @FXML Button zoomIn = new Button();
    @FXML Button zoomOut = new Button();
    @FXML Button invertColours = new Button();
    @FXML Button closeButton = new Button();

    public void zoomIn(ActionEvent actionEvent) {
        zoomIn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.getMainScene().getRoot().setScaleX(Main.getMainScene().getRoot().getScaleX()*1.25);
                Main.getMainScene().getRoot().setScaleY(Main.getMainScene().getRoot().getScaleY()*1.25);
                Main.getMainScene().getRoot().setScaleZ(Main.getMainScene().getRoot().getScaleZ()*1.25);
            }
        });
    }

    public void zoomOut(ActionEvent actionEvent) {
        zoomOut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.getMainScene().getRoot().setScaleX(Main.getMainScene().getRoot().getScaleX()*0.75);
                Main.getMainScene().getRoot().setScaleY(Main.getMainScene().getRoot().getScaleY()*0.75);
                Main.getMainScene().getRoot().setScaleZ(Main.getMainScene().getRoot().getScaleZ()*0.75);
            }
        });
    }

    public void invertColours(ActionEvent actionEvent) {
        invertColours.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (Main.getInvertedColours() == false) {
                    ColorInput colorInput = new ColorInput();
                    colorInput.setPaint(Color.WHITE);
                    colorInput.setWidth(Double.MAX_VALUE);
                    colorInput.setHeight(Double.MAX_VALUE);
                    Blend effect = new Blend(BlendMode.DIFFERENCE);
                    effect.setBottomInput(colorInput);
                    for (Window window : Stage.getWindows()) {
                        window.getScene().getRoot().setEffect(effect);
                    }
                    Main.setInvertedColours(true);
                }
                else if (Main.getInvertedColours() == true) {
                    ColorInput colorInput = new ColorInput();
                    colorInput.setPaint(Color.BLACK);
                    colorInput.setWidth(Double.MAX_VALUE);
                    colorInput.setHeight(Double.MAX_VALUE);
                    Blend effect = new Blend(BlendMode.DIFFERENCE);
                    effect.setBottomInput(colorInput);
                    for (Window window : Stage.getWindows()) {
                        window.getScene().getRoot().setEffect(effect);
                    }
                    Main.setInvertedColours(false);
                }
            }
        });
    }

    public void closeWindow(javafx.event.ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}
