package dashboard;

import POJOs.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import configuration.Configuration;
import metrics.Metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FilterTreeTest {
    private static DashboardModel model;
    private static String id_1, id_2, id_3, id_4, id_5;
    private static FilterTree<Multimap<String, ImpressionRecord>> tree;

    @BeforeEach
    public void setupTest() {
        // Create multimaps
        Multimap<String, Record> impressionRecordsMap = ArrayListMultimap.create();
        Multimap<String, Record> serverRecordsMap = ArrayListMultimap.create();
        Multimap<String, Record> clickRecordsMap = ArrayListMultimap.create();

        // Manually build records for use in tests to maintain a separation from the parser class
        id_1 = "97065088426436600";
        id_2 = "97065088426436601";
        id_3 = "97065088426436602";
        id_4 = "97065088426436603";
        id_5 = "97065088426436604";

        ImpressionRecord i1 = new ImpressionRecord(id_1,"2015-01-01 12:00:02" ,"Male","25-34","High","Blog","0.001713");
        ImpressionRecord i2 = new ImpressionRecord(id_2,"2015-02-02 14:12:05" ,"Female","<25","Medium","Social Media","0.000001");
        ImpressionRecord i3 = new ImpressionRecord(id_3,"2015-02-02 14:13:05" ,"Female","35-44","Medium","Shopping","0.000000");
        ImpressionRecord i4 = new ImpressionRecord(id_4,"2015-02-02 14:14:05" ,"Female","45-54","Low","News","0.000000");
        ImpressionRecord i5 = new ImpressionRecord(id_5,"2015-02-02 14:14:05" ,"Male",">54","Medium","Social Media","0.000000");

        // Add records to map
        impressionRecordsMap.put(i1.getId(), i1);
        impressionRecordsMap.put(i2.getId(), i2);
        impressionRecordsMap.put(i3.getId(), i3);
        impressionRecordsMap.put(i4.getId(), i4);
        impressionRecordsMap.put(i5.getId(), i5);

        Records records = new Records(impressionRecordsMap, serverRecordsMap, clickRecordsMap);
        Metrics metrics = new Metrics(records);
        Campaign campaign = new Campaign("random", records, metrics, "definition");
        FilterTreeTest.model = new DashboardModel();
        model.setCurrentCampaign(campaign);
        FilterTreeTest.tree = model.getFilterTree();
    }

    @Test
    @DisplayName("Test Filtering by Age, Income and Gender")
    public void tripleFilterTest() {
        String expected = id_2;

        try {
            tree.filter("age, <25");
            tree.filter("income, medium");
            tree.filter("gender, female");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
            return;
        }

        assertTrue(tree.getCurrentData().containsKey(expected) && tree.getCurrentData().size() == 1);
    }

    @Test
    @DisplayName("Test Filtering by Context")
    public void contextFilterTest() {
        String expected = id_4;

        try {
            tree.filter("context, news");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
            return;
        }

        assertTrue(tree.getCurrentData().containsKey(expected) && tree.getCurrentData().size() == 1);
    }

    @Test
    @DisplayName("Test Filtering by Both Audience Segments and Context")
    public void mixedFilterTest() {
        String expected = id_3;

        try {
            tree.filter("income, medium");
            tree.filter("context, shopping");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
            return;
        }

        assertTrue(tree.getCurrentData().containsKey(expected) && tree.getCurrentData().size() == 1);
    }

    @Test
    @DisplayName("Test Undo Filtering when Undoing Most Recent Filter Applied")
    public void undoSameFilterTest() {
        try {
            tree.filter("gender, female"); //TODO: undo non-existent filter (even though should be impossible on front end)
            tree.filter("age, <25");
            tree.undoFilter("age, <25");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
            return;
        }

        assertEquals(3, tree.getCurrentData().keySet().size());
    }

    @Test
    @DisplayName("Test Undo Filtering when Undoing First Filter Applied")
    public void undoDiffFilterTest() {
        try {
            tree.filter("income, medium");
            tree.filter("age, <25");
            tree.undoFilter("income, medium");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
            return;
        }

        assertEquals(1, tree.getCurrentData().keySet().size());
    }

    @Test
    @DisplayName("Test Undo Filtering Efficiency for Large Data Set")
    public void undoFilterEfficiencyTest() {
        DashboardModel model = null;
        FilterTree<Multimap<String, ImpressionRecord>> tree = null;
        String expected = id_2;

        User user = new User("David","123".getBytes(), "user");
        final String SERVER_LOG_CSV = "TestData/server_log.csv";
        final String IMPRESSION_LOG_CSV = "TestData/impression_log.csv";
        final String CLICK_LOG_CSV = "TestData/click_log.csv";
        try {
            Configuration config = new Configuration(user,SERVER_LOG_CSV,IMPRESSION_LOG_CSV,CLICK_LOG_CSV);
            Campaign campaign = config.buildDashboard().getCurrentCampaign();
            model = new DashboardModel();
            model.setCurrentCampaign(campaign);
            tree = model.getFilterTree();
        } catch (Exception e) {
            fail("Unexpected Exception Thrown");
            return;
        }

        try {
            tree.filter("income, medium");
            tree.filter("age, <25");
            tree.filter("gender, male");

            long t0 = System.currentTimeMillis();
            tree.undoFilter("age, <25");
            long t1 = System.currentTimeMillis(); System.out.println("Time for long undo: " + (t1 - t0));
            tree.filter("age, <25");
            long t2 = System.currentTimeMillis(); System.out.println("Time for long filter: " + (t2 - t1));
            tree.undoFilter("age, <25");
            long t3 = System.currentTimeMillis(); System.out.println("Time for short undo: " + (t3 - t2));
            tree.filter("age, <25");
            long t4 = System.currentTimeMillis(); System.out.println("Time for short filter: " + (t4 - t3));

            assertTrue((t3 - t2) < 100 && (t4 - t3) < 100);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
    }

    @Test
    @DisplayName("Test Date Filtering")
    public void dateFilterTest() {
        String expected = id_1;

        try {
            tree.filter("gender, male");
            tree.filterDate(LocalDate.of(2015,1,1), LocalDate.of(2015,1,1));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
            return;
        }

        assertTrue(tree.getCurrentData().containsKey(expected) && tree.getCurrentData().size() == 1);
    }

    @Test
    @DisplayName("Test Invalid Date Range when Filtering")
    public void invalidDateRangeTest() {
        try {
            tree.filterDate(LocalDate.of(2015,1,2), LocalDate.of(2015,1,1));
        } catch (FilterTree.InvalidDateRangeException e) {
            assertTrue(true);
            return;
        } catch (Exception e) {
            fail("Wrong exception thrown");
            return;
        }

        fail("No exception thrown");
    }
}
