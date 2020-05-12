package gui;

import gui.login.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.junit.Before;


import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;



import static org.testfx.api.FxAssert.verifyThat;


public class LoginGuiTest extends ApplicationTest {
    final String USER_ID ="#userField";
    final String PASSWORD_ID ="#passField";
    final String LOGIN_ADMIN ="#adminButton";
    public void start (Stage stage) throws Exception {

        Parent mainNode = FXMLLoader.load(Main.class.getResource("/fxml/Login.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }
    @Before
    public void setUp (){
    }

    @Test
    public void loginTest() {
        FxRobot bot = new FxRobot();
        bot.clickOn("#userField");
        sleep(2000);
        bot.lookup("#userField").queryAs(TextField.class).setText("admin");
        sleep(2000);
        bot.clickOn("#passField");
        bot.lookup("#passField").queryAs(TextField.class).setText("Admin2020");
        bot.clickOn("#LoginButton");

    }


    @Test
    public void  threeFilesPromptCheck() {


    }


}
