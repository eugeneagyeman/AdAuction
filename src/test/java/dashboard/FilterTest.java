package dashboard;

import POJOs.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import metrics.Metrics;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilterTest {
    private static DashboardModel model;
    private static String id_1, id_2, id_3, id_4, id_5;

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

        ClickRecord c1 = new ClickRecord(id_1,"2015-01-04 13:30:24","10.635709");
        ClickRecord c2 = new ClickRecord(id_1,"2015-01-12 14:13:05","9.468576");
        ClickRecord c3 = new ClickRecord(id_2,"2015-01-12 15:13:05","11.416722");

        ImpressionRecord i1 = new ImpressionRecord(id_1,"2015-01-01 12:00:02" ,"Male","25-34","High","Blog","0.001713");
        ImpressionRecord i2 = new ImpressionRecord(id_2,"2015-02-02 14:12:05" ,"Female","<25","Medium","Social Media","0.000001");
        ImpressionRecord i3 = new ImpressionRecord(id_3,"2015-02-02 14:13:05" ,"Female","35-44","Medium","Shopping","0.000000");
        ImpressionRecord i4 = new ImpressionRecord(id_4,"2015-02-02 14:14:05" ,"Female","45-54","Low","News","0.000000");
        ImpressionRecord i5 = new ImpressionRecord(id_5,"2015-02-02 14:14:05" ,"Male",">54","Medium","Social Media","0.000000");

        ServerRecord s1 = new ServerRecord(id_1,"2015-01-01 12:01:21","2015-01-01 12:05:13","7","No");
        ServerRecord s2 = new ServerRecord(id_2,"2015-02-02 14:12:05","2015-02-02 14:12:09","1","No");
        ServerRecord s3 = new ServerRecord(id_3,"2015-02-02 14:12:05","2015-02-02 14:12:09","1","Yes");
        ServerRecord s4 = new ServerRecord(id_4,"2015-01-01 12:04:13","2015-01-01 12:05:20","4","Yes");
        ServerRecord s5 = new ServerRecord(id_5,"2015-01-01 13:04:13","2015-01-01 13:06:48","6","No"); //TODO: Add test for exitDate < entryDate

        // Add records to maps
        clickRecordsMap.put(c1.getId(), c1);
        clickRecordsMap.put(c2.getId(), c2);
        clickRecordsMap.put(c3.getId(), c3);
        impressionRecordsMap.put(i1.getId(), i1);
        impressionRecordsMap.put(i2.getId(), i2);
        impressionRecordsMap.put(i3.getId(), i3);
        impressionRecordsMap.put(i4.getId(), i4);
        impressionRecordsMap.put(i5.getId(), i5);
        serverRecordsMap.put(s1.getId(), s1);
        serverRecordsMap.put(s2.getId(), s2);
        serverRecordsMap.put(s3.getId(), s3);
        serverRecordsMap.put(s4.getId(), s4);
        serverRecordsMap.put(s5.getId(), s5);

        Records records = new Records(impressionRecordsMap, serverRecordsMap, clickRecordsMap);
        Metrics metrics = new Metrics(records);
        Campaign campaign = new Campaign("random", records, metrics, "definition");
        FilterTest.model = new DashboardModel();
        model.setCurrentCampaign(campaign);
    }

    @ParameterizedTest
    @ValueSource(strings = {"<25", "25-34", "35-44", "45-54", ">54"})
    @DisplayName("Test Filtering by Age")
    public void impressionsAgeFilterTest(String ageRange) {
        Multimap<String, Record> filteredTestMap = model.getFilter().impressionsAgeFilter(ageRange);
        String expected = null;
        switch (ageRange) {
            case "<25":
                expected = id_2;
                break;
            case "25-34":
                expected = id_1;
                break;
            case "35-44":
                expected = id_3;
                break;
            case "45-54":
                expected = id_4;
                break;
            case ">54":
                expected = id_5;
                break;
        }
        assertTrue(filteredTestMap.containsKey(expected));
    }
}
