//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gui.upload;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import gui.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

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
        this.dragField.setOnDragOver((event) -> {
            if (event.getGestureSource() != this.dragField && event.getDragboard().hasFiles()) {
                if (!this.validExtensions.containsAll(event.getDragboard().getFiles().stream().map((file) -> {
                    return this.getExtension(file.getName());
                }).collect(Collectors.toList()))) {
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
        int i = fileName.lastIndexOf(46);
        return i > 0 && i < fileName.length() - 1 ? fileName.substring(i + 1).toLowerCase() : extension;
    }

    public void handleDrop(DragEvent event) throws IOException {
        Dragboard db = event.getDragboard();
        List<File> selectedFiles = db.getFiles();
        boolean success = false;
        if (db.hasFiles()) {
            this.itemsList.getItems().add(((File)selectedFiles.get(0)).getName());
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
        this.filesSelected();
    }

    public void ButtonAction() throws IOException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new ExtensionFilter[]{new ExtensionFilter("CSV Files", new String[]{"*.csv"})});
        List<File> selectedFiles = fc.showOpenMultipleDialog((Window)null);
        if (selectedFiles != null) {
            Iterator var3 = selectedFiles.iterator();

            while(var3.hasNext()) {
                File selectedFile = (File)var3.next();
                this.itemsList.getItems().add(selectedFile.getName());
                this.filesSelected();
            }
        } else {
            System.out.println("File invalid");
        }

    }

    public void filesSelected() throws IOException {
        if (this.itemsList.getItems().contains("server_log.csv") && this.itemsList.getItems().contains("impression_log.csv") && this.itemsList.getItems().contains("click_log.csv")) {
            Stage mainWindow = new Stage();
            Main.changeSceneAndResize("/fxml/Overview.fxml");
        } else {
            this.csvLabel.setText("Please upload click, server and impression log csv files");
        }

    }
}
