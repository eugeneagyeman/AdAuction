package gui.configuration;

import gui.Controller;
import gui.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ConfigController extends Controller {
    Parent root;
    Stage primaryStage;
    @FXML private Text usernameText = new Text();
    @FXML private Text userIDText = new Text();
    @FXML private Text currentCampaignText = new Text();
    @FXML private Text dateTimeText = new Text();
    @FXML private Text configText = new Text();
    @FXML private Button saveAsPDFButton = new Button();
    @FXML private Button printButton = new Button();
    @FXML private TextField currentDate = new TextField();

    public void initialiseConfiguration() {
        usernameText.setText(Main.getLogin().getCurrentUser().getUsername());
        userIDText.setText(String.valueOf(Main.getLogin().getUserID()));
        currentCampaignText.setText(Main.getModel().getCurrentCampaign().getCampaignID());
        dateTimeText.setText(Main.getLogin().getUsers().get(Main.getLogin().getCurrentUser()));
        configText.setText("");
        currentDate.setText(LocalDate.now().toString());
    }

    public void saveAsPDF(ActionEvent actionEvent) {
        saveAsPDFButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });
    }

    public void printCharts(ActionEvent actionEvent) {
        printButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                Printer defaultprinter = Printer.getDefaultPrinter();
//                PrinterJob job = PrinterJob.createPrinterJob();
//                configText.setText("Generating documents...");
//                try {
//                    Main.changeScene("/fxml/AudienceSegments.fxml");
//                    job.printPage((Node)actionEvent.getSource());
//                    //Main.changeScene("/fxml/Context.fxml");
//                    //job.printPage((Node)actionEvent.getSource());
//                    Main.changeScene("/fxml/Configuration.fxml");
//                } catch (IOException e) { e.printStackTrace(); }
//                configText.setText("Done");
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseConfiguration();
    }
}
