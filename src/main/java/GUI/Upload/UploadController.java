package GUI.Upload;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UploadController {

    List<String> validExtensions = Collections.singletonList("csv");

    @FXML
    private Label csvLabel;

    @FXML
    private ListView<String> itemsList;

    @FXML
    private ImageView dragField;

    @FXML
    private void handleDragOver() {
        dragField.setOnDragOver(event -> {
            // On drag over if the DragBoard has files
            if (event.getGestureSource() != dragField && event.getDragboard().hasFiles()) {
                // All files on the dragboard must have an accepted extension
                if (!validExtensions.containsAll(
                        event.getDragboard().getFiles().stream()
                                .map(file -> getExtension(file.getName()))
                                .collect(Collectors.toList()))) {
                    event.consume();
                    return;
                }
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    private String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) //if the name is not empty
            return fileName.substring(i + 1).toLowerCase();

        return extension;
    }

    public void handleDrop(DragEvent event) throws IOException {
        Dragboard db = event.getDragboard();
        List<File> selectedFiles = db.getFiles();
        boolean success = false;
        if (db.hasFiles()) {
            itemsList.getItems().add(selectedFiles.get(0).getName());
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
        filesSelected();
    }

    public void ButtonAction() throws IOException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        List<File> selectedFiles = fc.showOpenMultipleDialog(null);
        if (selectedFiles != null) {
            for (File selectedFile : selectedFiles) {
                itemsList.getItems().add(selectedFile.getName());
                filesSelected();
            }
        } else {
            System.out.println("File invalid");
        }
    }

    public void filesSelected() throws IOException {
        if (itemsList.getItems().contains("server_log.csv") && itemsList.getItems().contains("impression_log.csv") && itemsList.getItems().contains("click_log.csv")) {
            Stage mainWindow = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/overview.fxml"));
            Scene scene = new Scene(loader.load());
            mainWindow.setScene(scene);
            mainWindow.show();
        } else {
            csvLabel.setText("Please upload click, server and impression log csv files");
        }
    }
}