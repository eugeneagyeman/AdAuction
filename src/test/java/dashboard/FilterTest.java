package dashboard;

import POJOs.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import metrics.Metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilterTest {
    private static DashboardModel model;
    private static String id_1, id_2, id_3, id_4, id_5;

    @BeforeEach
    public void setupTest() {
        // Create multimaps
        Multimap<String, ImpressionRecord> impressionRecordsMap = ArrayListMultimap.create();
        Multimap<String, ServerRecord> serverRecordsMap = ArrayListMultimap.create();
        Multimap<String, ClickRecord> clickRecordsMap = ArrayListMultimap.create();

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
        Multimap<String, ImpressionRecord> filteredTestMap = model.getFilter()
                .impressionsAgeFilter(ageRange, model.getCurrentCampaign().getRecords().getImpressionRecords());
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

    @ParameterizedTest
    @ValueSource(strings = {"Male", "Female"})
    @DisplayName("Test Filtering by Gender")
    public void impressionsGenderFilterTest(String gender) {
        Multimap<String, ImpressionRecord> filteredTestMap = model.getFilter()
                .impressionsGenderFilter(gender, model.getCurrentCampaign().getRecords().getImpressionRecords());
        ArrayList<String> expected = new ArrayList<>();
        switch (gender) {
            case "Male":
                expected.add(id_1); expected.add(id_5);
                break;
            case "Female":
                expected.add(id_2); expected.add(id_3);
                expected.add(id_4);
                break;
        }
        boolean match = true;
        for (String exp : expected) {
            if (!filteredTestMap.containsKey(exp)) {
                match = false;
            }
        } if (filteredTestMap.keySet().size() != expected.size())
            match = false;

        assertTrue(match);
    }

    @ParameterizedTest
    @ValueSource(strings = {"High", "Medium", "Low"})
    @DisplayName("Test Filtering by Income")
    public void impressionsIncomeFilterTest(String income) {
        Multimap<String, ImpressionRecord> filteredTestMap = model.getFilter()
                .impressionsIncomeFilter(income, model.getCurrentCampaign().getRecords().getImpressionRecords());
        ArrayList<String> expected = new ArrayList<>();
        switch (income) {
            case "High":
                expected.add(id_1);
                break;
            case "Medium":
                expected.add(id_2); expected.add(id_3);
                expected.add(id_5);
                break;
            case "Low":
                expected.add(id_4);
                break;
        }
        boolean match = true;
        for (String exp : expected) {
            if (!filteredTestMap.containsKey(exp)) {
                match = false;
            }
        } if (filteredTestMap.keySet().size() != expected.size())
            match = false;

        assertTrue(match);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Blog", "Social Media", "Shopping", "News"})
    @DisplayName("Test Filtering by Context")
    public void impressionsContextFilterTest(String context) {
        Multimap<String, ImpressionRecord> filteredTestMap = model.getFilter()
                .contextFilter(context, model.getCurrentCampaign().getRecords().getImpressionRecords());
        ArrayList<String> expected = new ArrayList<>();
        switch (context) {
            case "Blog":
                expected.add(id_1);
                break;
            case "Social Media":
                expected.add(id_2);
                expected.add(id_5);
                break;
            case "Shopping":
                expected.add(id_3);
                break;
            case "News":
                expected.add(id_4);
                break;
        }
        boolean match = true;
        for (String exp : expected) {
            if (!filteredTestMap.containsKey(exp)) {
                match = false;
            }
        } if (filteredTestMap.keySet().size() != expected.size())
            match = false;

        assertTrue(match);
        ImpressionRecord i1 = new ImpressionRecord(id_1,"2015-01-01 12:00:02" ,"Male","25-34","High","Blog","0.001713");
        ImpressionRecord i2 = new ImpressionRecord(id_2,"2015-02-02 14:12:05" ,"Female","<25","Medium","Social Media","0.000001");
        ImpressionRecord i3 = new ImpressionRecord(id_3,"2015-02-02 14:13:05" ,"Female","35-44","Medium","Shopping","0.000000");
        ImpressionRecord i4 = new ImpressionRecord(id_4,"2015-02-02 14:14:05" ,"Female","45-54","Low","News","0.000000");
        ImpressionRecord i5 = new ImpressionRecord(id_5,"2015-02-02 14:14:05" ,"Male",">54","Medium","Social Media","0.000000");
    }
}
