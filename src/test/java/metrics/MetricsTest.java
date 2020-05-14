package metrics;

import POJOs.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MetricsTest {
    private static Metrics metrics;

    @BeforeEach
    public void setupTest() {
        // Create multimaps
        Multimap<String, ImpressionRecord> impressionRecordsMap = ArrayListMultimap.create();
        Multimap<String, ServerRecord> serverRecordsMap = ArrayListMultimap.create();
        Multimap<String, ClickRecord> clickRecordsMap = ArrayListMultimap.create();

        // Manually build records for use in tests to maintain a separation from the parser class
        String id_1 = "97065088426436600";
        String id_2 = "97065088426436601";
        ClickRecord c1 = new ClickRecord(id_1,"2015-01-04 13:30:24","10.635709");
        ClickRecord c2 = new ClickRecord(id_1,"2015-01-12 14:13:05","9.468576");
        ClickRecord c3 = new ClickRecord(id_2,"2015-01-12 15:13:05","11.416722");
        ImpressionRecord i1 = new ImpressionRecord(id_1,"2015-01-01 12:00:02" ,"Male","25-34","High","Blog","0.001713");
        ImpressionRecord i2 = new ImpressionRecord(id_2,"2015-02-02 14:12:05" ,"Female","<25","Medium","Social Media","0.000001");
        ImpressionRecord i3 = new ImpressionRecord(id_2,"2015-02-02 14:13:05" ,"Female","<25","Medium","Social Media","0.000000");
        ImpressionRecord i4 = new ImpressionRecord(id_2,"2015-02-02 14:14:05" ,"Female","<25","Medium","Social Media","0.000000");
        ServerRecord s1 = new ServerRecord(id_1,"2015-01-01 12:01:21","2015-01-01 12:05:13","7","No");
        ServerRecord s2 = new ServerRecord(id_2,"2015-02-02 14:12:05","2015-02-02 14:12:09","1","No");
        ServerRecord s3 = new ServerRecord(id_2,"2015-02-02 14:12:05","2015-02-02 14:12:09","1","Yes");
        ServerRecord s4 = new ServerRecord(id_2,"2015-01-01 12:04:13","2015-01-01 12:05:20","4","Yes");

        // Add records to maps
        clickRecordsMap.put(c1.getId(), c1);
        clickRecordsMap.put(c2.getId(), c2);
        clickRecordsMap.put(c3.getId(), c3);
        impressionRecordsMap.put(i1.getId(), i1);
        impressionRecordsMap.put(i2.getId(), i2);
        impressionRecordsMap.put(i3.getId(), i3);
        impressionRecordsMap.put(i4.getId(), i4);
        serverRecordsMap.put(s1.getId(), s1);
        serverRecordsMap.put(s2.getId(), s2);
        serverRecordsMap.put(s3.getId(), s3);
        serverRecordsMap.put(s4.getId(), s4);

        Records records = new Records(impressionRecordsMap, serverRecordsMap, clickRecordsMap);
        MetricsTest.metrics = new Metrics(records);
    }

    @Test
    @DisplayName("Test Number of Clicks")
    public void numOfClicksTest() {

        /* TODO: click cost
        assertEquals(c1.getClickCost(), 10.635709);
        assertEquals(c1.getClickCost(), 9.468576);*/

        assertEquals(2, metrics.calculateNumOfClicks());

        /*model = new Configuration().buildDashboard();
        Metrics metric1=model.getMetrics();
        assertEquals(metric1.calculateNumOfClicks(),23924);*/
    }

    @Test
    @DisplayName("Test Number of Uniques")
    public void numOfUniquesTest() {
        assertEquals(2, metrics.calculateNumOfUniques());
    }

    @Test
    @DisplayName("Test Number of Impressions")
    public void numOfImpressionsTest() {
        assertEquals(2, metrics.getNumOfImpressions());
    }

    @Test
    @DisplayName("Test Number of Conversions")
    public void numOfConversionsTest() {
        assertEquals(2, metrics.getNumOfConversions());
    }

    @Test
    @DisplayName("Test Number of Bounces")
    public void numOfBouncesTest() {
        assertEquals(1, metrics.calculateNumOfBounces());
    }

    @Test
    @DisplayName("Test Bounce Rate")
    public void bounceRateTest() {
        assertEquals(50.0, round_6(metrics.calculateBouncerate()));
    }

    @Test
    @DisplayName("Test Total Impression Cost")
    public void  TotalCostTest() {
        assertEquals(round_6(0.001714), round_6(metrics.calculateTotalCost()));
    }

    @Test
    @DisplayName("Test Average Number of Clicks per Impression")
    public void ctrTest() {
        assertEquals(round_6(100.0), round_6(metrics.calculateClickThroughRate()));
    }

    @Test
    @DisplayName("Test Cost per Action")
    public void cpaTest() {
        assertEquals(round_6(0.001714/2), round_6(metrics.calculateCostPerAction()));
    }

    @Test
    @DisplayName("Test Cost per Click")
    public void cpcTest() {
        assertEquals(round_6(8.57E-4), round_6(metrics.calculateCostPerClick()));
    }

    @Test
    @DisplayName("Test Cost per Thousand Impressions")
    public void cpmTest() {
        assertEquals(round_6(0.857), round_6(metrics.calculateCostPerThousand()));
    }

    private double round_6(double v) {
        BigDecimal bd = new BigDecimal(Double.toString(v));
        bd = bd.setScale(6, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
