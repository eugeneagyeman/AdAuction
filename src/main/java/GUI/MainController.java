package GUI;

import java.io.IOException;

public class MainController<Pane> {

    public void overviewButtonClicked(javafx.event.ActionEvent actionEvent) throws IOException {
        GUI.Main.changeScene("Overview.fxml");

    }
}

