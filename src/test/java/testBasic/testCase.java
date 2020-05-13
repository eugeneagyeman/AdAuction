package testBasic;


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
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

public class testCase {
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
    public void setUp() throws IOException {
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
    public void invalidLoginTest() {
        assertNull(login.login("1", "Admin2020", "admin"));
    }

    @Test(priority = 2)
    public void invalidRegisterTest() {
        System.out.println("Register for new User");
        assertNull(login.addUser("","123","admin"));
    }

    @Test(priority = 3)
    public void registerTest() {
        testCase.login.addUser("user", "User2020", "user");

    }

    @Test(priority = 4)
    public void LoginTest() {
        user=testCase.login.login("user", "User2020", "user");
    }

    @Test(priority = 5)
    public void userTest() {
        Assert.assertEquals( user.getUsername(),"user");
        Assert.assertEquals( user.getPassword(),"User2020");
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

    @Test(priority = 8)
    public void profileTest() {
        Assert.assertEquals( login.getUserID(),2);
        Assert.assertEquals( user.getUsername(),"user");
    }

    @Test(priority = 7)
    public void numOfClicksTest() {
        Assert.assertEquals( metrics.calculateNumOfClicks(),23923);
        System.out.print("Correct numOfClicks");
    }

    @Test(priority = 7)
    public void numOfUniquesTest() {
        Assert.assertEquals( metrics.calculateNumOfUniques(),442458);
        System.out.print("Correct numOfUniques");
    }

    @Test(priority = 7)
    public void numOfImpressionsTest() {
        Assert.assertEquals( metrics.getNumOfImpressions(),486104);
        System.out.print("Correct numOfImpressions");
    }

    @Test(priority = 7)
    public void numOfConversionsTest() {
        Assert.assertEquals(metrics.getNumOfConversions(),2026);
        System.out.print("Correct numOfConversions");
    }

    @Test(priority = 7)
    public void numOfBouncesTest() {
        Assert.assertEquals(metrics.calculateNumOfBounces(),4260);
        System.out.print(" Correct NumOfBounces");
    }

    @Test(priority = 7)
    public void bounceRateTest() {
        Assert.assertEquals(round_4(metrics.calculateBouncerate()),round_4(4260.0 / 23923.0 * 100.0));
        System.out.print("Correct bounceRate");
    }

    @Test(priority = 7)
    public void  TotalCostTest() {
        Assert.assertEquals( round_4(metrics.calculateTotalCost()),round_4(487.0554));
        System.out.print("Correct TotalCost");
    }

    @Test(priority = 7)
    public void ctrTest() {
        Assert. assertEquals(round_4(metrics.calculateClickThroughRate()),round_4(4.9214));
        System.out.print("Correct ctr");
    }

    @Test(priority = 7)
    public void cpaTest() {
        Assert. assertEquals(round_4(metrics.calculateCostPerAction()),round_4(0.2404));
        System.out.print("Correct cpaTest");
    }

    @Test(priority = 7)
    public void cpcTest() {
        Assert.assertEquals( round_4(metrics.calculateCostPerClick()),round_4(0.0204));
        System.out.print("Correct Cost per Click");
    }

    @Test(priority = 7)
    public void cpmTest() {
        Assert.assertEquals( round_4(metrics.calculateCostPerThousand()),round_4(1.002));
        System.out.print("Correct Cost per Thousand Impressions");
    }


    private double round_4(double v) {
        BigDecimal bd = new BigDecimal(Double.toString(v));
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }




}
