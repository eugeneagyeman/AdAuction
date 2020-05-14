import POJOs.Campaign;
import POJOs.Records;
import POJOs.User;
import configuration.Configuration;
import dashboard.DashboardModel;
import dashboard.Filter;
import dashboard.FilterTree;
import gui.login.LoginController;
import login.Login;
import metrics.Metrics;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertNull;

public class IntegrationTest {


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

    @BeforeClass
    public void setUp() throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        System.out.println("Method---setup");
        login =new Login();
        config=new Configuration();
        dashboard=config.buildDashboard(TEST_DATA_IMPRESSION_LOG_CSV, TEST_DATA_SERVER_LOG_CSV, TEST_DATA_CLICK_LOG_CSV);
        metrics=dashboard.getMetrics();
        campaign=dashboard.getCurrentCampaign();
        filter=dashboard.getFilter();
        filterTree=dashboard.getFilterTree();
        login.logout();
    }

    @Test(priority = 1)
    public void invalidLoginTest() throws BadPaddingException, IllegalBlockSizeException {
        assertNull(login.login("1", "Admin2020", "admin"));
    }

    @Test(priority = 2)

    public void invalidRegisterTest() throws BadPaddingException, IllegalBlockSizeException {
        System.out.println("Register for new User");
        assertNull(login.addUser("","123","admin"));
    }

    @Test(priority = 3)
    public void registerControllerTest() throws BadPaddingException, IllegalBlockSizeException {
        LoginController lC =new LoginController();

    }

    @Test(priority = 3)
    public void registerTest() throws BadPaddingException, IllegalBlockSizeException {
        IntegrationTest.login.addUser("user", "User2020", "user");

    }

    @Test(priority = 4)
    public void LoginTest() throws BadPaddingException, IllegalBlockSizeException {
        user=IntegrationTest.login.login("user", "User2020", "user");
    }

    @Test(priority = 5)
    public void userTest() {
        Assert.assertEquals( user.getUsername(),"user");
        Assert.assertEquals( user.getType(),"user");
    }

    @Test(priority = 6)
    public void uploadFile() throws IOException {
        System.out.println("choose three file ");
        config.buildDashboard(TEST_DATA_IMPRESSION_LOG_CSV, TEST_DATA_SERVER_LOG_CSV, TEST_DATA_CLICK_LOG_CSV);
        System.out.println("successfully loading 3 file");
    }

    @Test(priority = 9)
    public void logOutTest() throws IOException {
        login.logout();
    }

    @Test(priority = 7)
    public void metricTest() {
        Assert.assertEquals( metrics.calculateNumOfClicks(),23806);
        System.out.print("Correct numOfClicks");

        Assert.assertEquals( metrics.calculateNumOfUniques(),442458);
        System.out.print("Correct numOfUniques");

        Assert.assertEquals( metrics.getNumOfImpressions(),439832);
        System.out.print("Correct numOfImpressions");

        Assert.assertEquals(metrics.getNumOfConversions(),2026);
        System.out.print("Correct numOfConversions");

        Assert.assertEquals(metrics.calculateNumOfBounces(),4260);
        System.out.print(" Correct NumOfBounces");

        Assert.assertEquals(round_4(metrics.calculateBouncerate()),round_4(17.8946));
        System.out.print("Correct bounceRate");


        Assert.assertEquals( round_4(metrics.calculateTotalCost()),round_4(487.0556));
        System.out.print("Correct TotalCost");

        Assert. assertEquals(round_4(metrics.calculateClickThroughRate()),round_4(5.4125));
        System.out.print("Correct ctr");


        Assert. assertEquals(round_4(metrics.calculateCostPerAction()),round_4(0.2404));
        System.out.print("Correct cpaTest");


        Assert.assertEquals( round_4(metrics.calculateCostPerClick()),round_4(0.0205));
        System.out.print("Correct Cost per Click");

        Assert.assertEquals( round_4(metrics.calculateCostPerThousand()),round_4(1.1074));
        System.out.print("Correct Cost per Thousand Impressions");
    }

    private double round_4(double v) {
        BigDecimal bd = new BigDecimal(Double.toString(v));
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}