package gui.configuration;

import gui.Controller;
import gui.Main;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ConfigController extends Controller {
    Parent root;
    Stage primaryStage;
    @FXML private Text usernameText;
    @FXML private Text userIDText;
    @FXML private Text currentCampaignText;
    @FXML private Text dateTimeText;
    @FXML private Text configText;
    @FXML private Button saveAsPDFButton;
    @FXML private Button printButton;
    @FXML private TextField currentDate;

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
                try {
                    PDDocument document = createPDF();
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                    fileChooser.setInitialFileName("AdAuctionData");
                    File file = fileChooser.showSaveDialog(primaryStage);
                    File directory = file.getParentFile();
                    String filename = fileChooser.getInitialFileName();
                    if (directory.isDirectory()) {
                        if (filename.contains(".pdf")) {
                            document.save(directory + "/" + filename);
                            document.close();
                        }
                        else {
                            document.save(directory + "/" + filename + ".pdf");
                            document.close();
                        }
                    }
                } catch (IOException e) { e.printStackTrace(); }
            }
        });
    }

    public void printCharts(ActionEvent actionEvent) {
        printButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    root = FXMLLoader.load(this.getClass().getResource("/fxml/PrintPreview.fxml"));
                    primaryStage = new Stage();
                    primaryStage.setTitle("Print");
                    primaryStage.setScene(new Scene(root, 600.0D, 600.0D));
                    primaryStage.show();
                } catch (IOException e) { e.printStackTrace(); }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Main.getModel();
        initialiseController();
        initialiseConfiguration();
    }

    public PDDocument createPDF() throws IOException {
        root = FXMLLoader.load(this.getClass().getResource("/fxml/Print.fxml"));
        primaryStage = new Stage();
        primaryStage.setTitle("Print");
        primaryStage.setScene(new Scene(root));

        WritableImage image = root.snapshot(new SnapshotParameters(),null);
        File file = new File("image.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            Image imageScaled = ImageIO.read(file);
            final BufferedImage bufferedImage = new BufferedImage(500,700, BufferedImage.TYPE_INT_RGB);
            final Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setComposite(AlphaComposite.Src);
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.drawImage(imageScaled, 0, 0, 500, 700, null);
            graphics2D.dispose();
            File file2 = new File("imageScaled.png");
            ImageIO.write(bufferedImage,"png",file2);
        } catch (IOException e) {}


        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage();
        PDPage page2 = new PDPage();
        document.addPage(page1);
        document.addPage(page2);
        PDPageContentStream contentStream1 = new PDPageContentStream(document,page1);

        contentStream1.beginText();
        contentStream1.setLeading(25f);
        contentStream1.newLineAtOffset(40, 700);
        contentStream1.setFont(PDType1Font.HELVETICA_BOLD, 24);
        contentStream1.showText("Ad Auction - Data"); contentStream1.newLine();

        contentStream1.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream1.showText("SEG Team 38"); contentStream1.newLine();

        contentStream1.setFont(PDType1Font.HELVETICA, 14); contentStream1.newLine();
        contentStream1.showText("Total Clicks: " + Main.getModel().getMetrics().getNumOfClicks()); contentStream1.newLine();
        contentStream1.showText("Cost Per Click: " + Main.getModel().getMetrics().getCostPerClick()); contentStream1.newLine();
        contentStream1.showText("Cost Per Thousand Impressions: " + Main.getModel().getMetrics().getCostPerThousand()); contentStream1.newLine();
        contentStream1.showText("Total Impressions: " + Main.getModel().getMetrics().getNumOfImpressions()); contentStream1.newLine();
        contentStream1.showText("Total Conversions: " + Main.getModel().getMetrics().getNumOfConversions()); contentStream1.newLine();
        contentStream1.showText("Bounce Rate: " + Main.getModel().getMetrics().getBounceRate()); contentStream1.newLine();
        contentStream1.showText("Total Bounces: " + Main.getModel().getMetrics().getNumOfBounces()); contentStream1.newLine();
        contentStream1.showText("Click Through Rate: " + Main.getModel().getMetrics().getClickThroughRate()); contentStream1.newLine();
        contentStream1.showText("Number of Uniques: " + Main.getModel().getMetrics().getNumOfUniques()); contentStream1.newLine();
        contentStream1.endText();
        contentStream1.close();

        PDPageContentStream contentStream2 = new PDPageContentStream(document,page2);
        PDImageXObject img = PDImageXObject.createFromFile("imageScaled.png",document);
        contentStream2.drawImage(img,60,60);
        contentStream2.close();
        document.save("AdAuctionPrint.pdf");
        return document;
    }
}
