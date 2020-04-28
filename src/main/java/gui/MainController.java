package gui;

import java.io.IOException;

public class MainController<Pane> {

    public void overviewButtonClicked(javafx.event.ActionEvent actionEvent) throws IOException {
        gui.Main.changeScene("Overview.fxml");

    }
}

