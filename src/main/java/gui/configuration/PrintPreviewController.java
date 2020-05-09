package gui.configuration;

import gui.Main;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.printing.PDFPageable;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrintPreviewController implements Initializable {
    Parent root;
    Stage primaryStage;
    @FXML private VBox graphs = new VBox(10);
    @FXML private Text chunk1 = new Text();
    @FXML private Text chunk2 = new Text();
    @FXML private Text chunk3 = new Text();
    @FXML private Text chunk4 = new Text();
    @FXML private Text chunk5 = new Text();
    @FXML private Text chunk6 = new Text();
    @FXML private Text chunk7 = new Text();
    @FXML private Text chunk8 = new Text();
    @FXML private Text chunk9 = new Text();

    public void initializePrintController() {
        chunk1.setText("Total Clicks: " + Main.getModel().getMetrics().getNumOfClicks());
        chunk2.setText("Cost Per Click: " + Main.getModel().getMetrics().getCostPerClick());
        chunk3.setText("Cost Per Thousand Impressions: " + Main.getModel().getMetrics().getCostPerThousand());
        chunk4.setText("Total Impressions: " + Main.getModel().getMetrics().getNumOfImpressions());
        chunk5.setText("Total Conversions: " + Main.getModel().getMetrics().getNumOfConversions());
        chunk6.setText("Bounce Rate: " + Main.getModel().getMetrics().getBounceRate());
        chunk7.setText("Total Bounces: " + Main.getModel().getMetrics().getNumOfBounces());
        chunk8.setText("Click Through Rate: " + Main.getModel().getMetrics().getClickThroughRate());
        chunk9.setText("Number of Uniques: " + Main.getModel().getMetrics().getNumOfUniques());
        graphs.getChildren().addAll(Main.getModel().getChartMetrics().getAllCharts());
        graphs.getChildren().forEach(node -> {
            node.scaleXProperty();
            node.scaleYProperty();
            node.scaleZProperty();
        });
    }

    public void print(javafx.event.ActionEvent actionEvent) throws IOException, PrintException, PrinterException {
        createPDF();
        PDDocument doc = PDDocument.load(new File("AdAuctionPrint.pdf"));
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(doc));
        job.print();
        doc.close();
    }

    public void closeWindow(javafx.event.ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializePrintController();
    }

    public void createPDF() throws IOException {
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
        document.close();
    }

}
