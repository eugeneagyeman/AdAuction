package GUI;

import Dashboard.DashboardModel;
import POJOs.Campaign;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    protected DashboardModel model;

    @FXML protected Accordion accordion = new Accordion();
    @FXML protected TitledPane overviewTab = new TitledPane();
    @FXML protected TitledPane audienceSegmentsTab = new TitledPane();
    @FXML protected TitledPane contextTab = new TitledPane();
    @FXML protected TitledPane configurationTab = new TitledPane();
    @FXML protected TitledPane campaignsTab = new TitledPane();
    @FXML protected ListView<RadioButton> campaignsList = new ListView<>();
    @FXML protected TextField currentDate = new TextField();

    public void initialiseController() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}