package gui;

import dashboard.DashboardModel;
import POJOs.Campaign;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    Parent root;
    Stage primaryStage;
    protected DashboardModel model;
    @FXML protected VBox box;
    @FXML protected Accordion accordion;
    @FXML protected TitledPane overviewTab;
    @FXML protected TitledPane audienceSegmentsTab;
    @FXML protected TitledPane contextTab;
    @FXML protected TitledPane configurationTab;
    @FXML protected TitledPane campaignsTab;
    @FXML protected ListView<RadioButton> campaignsList = new ListView<>();
    @FXML protected TextField currentDate;
    @FXML Button profileButton;
    @FXML Button settingsButton;

    public void initialiseController() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate.setText(dateFormat.format(new Date()));

        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        for (Campaign campaign : model.getListOfCampaigns()) {
            RadioButton radioButton = new RadioButton();
            radioButton.setText(campaign.getCampaignID());
            radioButtons.add(radioButton);
        }
        campaignsList.setItems(FXCollections.observableArrayList(radioButtons));

        overviewTab.setOnMouseClicked(event -> {
            try {
                Main.changeScene("/fxml/Overview.fxml");
            } catch (IOException i) {
                i.printStackTrace();
            }
        });
        audienceSegmentsTab.setOnMouseClicked(event -> {
            try {
                Main.changeScene("/fxml/AudienceSegments.fxml");
            } catch (IOException i) {
                i.printStackTrace();
            }
        });
        contextTab.setOnMouseClicked(event -> {
            try {
                Main.changeScene("/fxml/Context.fxml");
            } catch (IOException i) {
                i.printStackTrace();
            }
        });
        configurationTab.setOnMouseClicked(event -> {
            try {
                Main.changeScene("/fxml/Configuration.fxml");
            } catch (IOException i) {
                i.printStackTrace();
            }
        });
    }

    public void profilePopup(ActionEvent actionEvent) {
        profileButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    root = FXMLLoader.load(this.getClass().getResource("/fxml/LoginPopup.fxml"));
                    primaryStage = new Stage();
                    primaryStage.setTitle("Profile");
                    primaryStage.setScene(new Scene(root));
                    primaryStage.show();
                } catch (IOException e) { e.printStackTrace(); }
            }
        });
    }

    public void settingsPopup() {
        settingsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    root = FXMLLoader.load(this.getClass().getResource("/fxml/SettingsPopup.fxml"));
                    primaryStage = new Stage();
                    primaryStage.setTitle("Settings");
                    primaryStage.setScene(new Scene(root));
                    primaryStage.show();
                } catch (IOException e) { e.printStackTrace(); }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}