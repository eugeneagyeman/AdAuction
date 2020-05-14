package basic;

import POJOs.Campaign;
import POJOs.Records;
import POJOs.User;
import configuration.Configuration;
import dashboard.DashboardModel;
import dashboard.Filter;
import dashboard.FilterTree;
import login.Login;
import metrics.Metrics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class BasicTest {
    public static Login login;
    private static User user;
    private static Configuration config;
    private static DashboardModel dashboard;
    private static Metrics metrics;
    private static Records records;
    private static Campaign campaign;
    private static Filter filter;
    private static FilterTree filterTree;


    private final String TEST_DATA_CLICK_LOG_CSV = "TestData/click_log.csv";
    private final String TEST_DATA_SERVER_LOG_CSV = "TestData/server_log.csv";
    private final String TEST_DATA_IMPRESSION_LOG_CSV = "TestData/impression_log.csv";

    @BeforeEach
    public void setUpTest() throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        System.out.println("Method---setup");
        login = new Login();
        config = new Configuration();
        dashboard = config.buildDashboard(TEST_DATA_IMPRESSION_LOG_CSV, TEST_DATA_SERVER_LOG_CSV, TEST_DATA_CLICK_LOG_CSV);
        metrics = dashboard.getMetrics();
        campaign = dashboard.getCurrentCampaign();
        filter = dashboard.getFilter();
        filterTree = dashboard.getFilterTree();
        login.logout();
    }
    @Test
    public void invalidLoginTest() {
        try {
            assertNull(login.login("1", "Admin2020", "admin"));
        } catch (Exception e) {
            fail("Unexpected Exception Thrown");
        }
    }

    @Test
    public void invalidRegisterTest() {
        System.out.println("Register for new User");
        try {
            assertNull(login.addUser("","123","admin"));
        } catch (Exception e) {
            fail("Unexpected Exception Thrown");
        }
    }

    @Test
    public void registerTest() {
        try {
            BasicTest.login.addUser("user", "User2020", "user");
        } catch (Exception e) {
            fail("Unexpected Exception Thrown");
        }

    }

    @Test
    public void LoginTest() {
        try {
            user = BasicTest.login.login("user", "User2020", "user");
        } catch (Exception e) {
            fail("Unexpected Exception Thrown");
        }
    }

    @Test
    public void uploadFile() throws IOException {
        System.out.println("choose three file ");
        config.buildDashboard(TEST_DATA_IMPRESSION_LOG_CSV, TEST_DATA_SERVER_LOG_CSV, TEST_DATA_CLICK_LOG_CSV);
        System.out.println("successfully loading 3 file");
    }

    @Test
    public void logOutTest() {
        try {
            login.logout();
        } catch (Exception e) {
            fail("Unexpected Exception Thrown");
        }
    }
}
