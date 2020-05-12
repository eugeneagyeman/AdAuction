package testBasic;


import POJOs.User;
import configuration.Configuration;
import login.Login;
import metrics.Metrics;
import org.junit.jupiter.api.DisplayName;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testCase {
    private static Login login;
    private static Configuration c_1;
    private static Metrics metrics;
    private final String TEST_DATA_CLICK_LOG_CSV = "TestData/click_log.csv";
    private final String TEST_DATA_SERVER_LOG_CSV = "TestData/server_log.csv";
    private final String TEST_DATA_IMPRESSION_LOG_CSV = "TestData/impression_log.csv";

    @BeforeClass
    public void setUp() throws IOException {
        System.out.println("Method---setup");
        login =new Login();
        c_1=new Configuration();
        System.out.println("testRegister for default user1");
        User user=testCase.login.login("admin", "Admin2020", "admin");
        metrics=c_1.buildDashboard().getMetrics();
    }

    @Test
    public void numOfClicksTest() {
        Assert.assertEquals( metrics.calculateNumOfClicks(),23923);
        System.out.print("Correct numOfClicks");
    }

    @Test
    public void numOfUniquesTest() {
        Assert.assertEquals( metrics.calculateNumOfUniques(),442458);
        System.out.print("Correct numOfUniques");
    }

    @Test
    public void numOfImpressionsTest() {
        Assert.assertEquals( metrics.getNumOfImpressions(),486104);
        System.out.print("Correct numOfImpressions");
    }

    @Test
    public void numOfConversionsTest() {
        Assert.assertEquals(metrics.getNumOfConversions(),2026);
        System.out.print("Correct numOfConversions");
    }

    @Test
    public void numOfBouncesTest() {
        Assert.assertEquals(metrics.calculateNumOfBounces(),4260);
        System.out.print(" CorrectcalculateNumOfBounces");
    }

    @Test
    public void bounceRateTest() {
        Assert.assertEquals(round_4(metrics.calculateBouncerate()),round_4(4260.0 / 23923.0 * 100.0));
        System.out.print("Correct bounceRate");
    }

    @Test
    public void  TotalCostTest() {
        Assert.assertEquals( round_4(metrics.calculateTotalCost()),round_4(487.0554));
        System.out.print("Correct numOfClicksTest");
    }

    @Test
    public void ctrTest() {
        Assert. assertEquals(round_4(metrics.calculateClickThroughRate()),round_4(4.9214));
        System.out.print("Correct numOfClicksTest");
    }

    @Test
    public void cpaTest() {
        Assert. assertEquals(round_4(metrics.calculateCostPerAction()),round_4(0.2404));
        System.out.print("Correct numOfClicksTest is");
    }

    @Test
    @DisplayName("Test Cost per Click")
    public void cpcTest() {
        Assert.assertEquals( round_4(metrics.calculateCostPerClick()),round_4(0.0204));
        System.out.print("Correct numOfClicksTest is");
    }

    @Test
    @DisplayName("Test Cost per Thousand Impressions")
    public void cpmTest() {
        Assert.assertEquals( round_4(metrics.calculateCostPerThousand()),round_4(1.002));
        System.out.print("Correct numOfClicksTest is");
    }

    private double round_4(double v) {
        BigDecimal bd = new BigDecimal(Double.toString(v));
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
