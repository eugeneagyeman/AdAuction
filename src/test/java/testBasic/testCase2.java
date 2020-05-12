package testBasic;

import POJOs.User;
import configuration.Configuration;
import login.Login;
import metrics.Metrics;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.io.IOException;

public class testCase2{
    private static Login login;
    private static Configuration config;
    private static Metrics metrics;
    private final String TEST_DATA_CLICK_LOG_CSV = "TestData/click_log.csv";
    private final String TEST_DATA_SERVER_LOG_CSV = "TestData/server_log.csv";
    private final String TEST_DATA_IMPRESSION_LOG_CSV = "TestData/impression_log.csv";
    @BeforeClass
    public void setUp() throws IOException {
        System.out.println("Method---setup");
        login =new Login();
        config=new Configuration();
        System.out.println("testRegister for register  user2");
        metrics=config.buildDashboard().getMetrics();
    }
    @Test
    public void register(){
       login.addUser("user2020", "User2020", "user");
       login.login("user2020", "User2020", "user");

        System.out.println( login.getCurrentUser().getUsername()+" login");
    }


    @Test
    public void uploadFile() throws IOException {
        System.out.println("choose three file ");
        config.buildDashboard(TEST_DATA_IMPRESSION_LOG_CSV,TEST_DATA_SERVER_LOG_CSV,TEST_DATA_CLICK_LOG_CSV);
        System.out.println(TEST_DATA_IMPRESSION_LOG_CSV+" "+TEST_DATA_SERVER_LOG_CSV+" "+TEST_DATA_CLICK_LOG_CSV);
    }

}