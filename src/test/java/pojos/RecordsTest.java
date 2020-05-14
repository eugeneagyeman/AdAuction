package pojos;

import POJOs.ClickRecord;
import POJOs.ImpressionRecord;
import POJOs.Records;
import POJOs.ServerRecord;
import POJOs.Campaign;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dashboard.DashboardModel;
import metrics.Metrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;

public class RecordsTest {

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

        ImpressionRecord i1 = new ImpressionRecord(id_1,"2015-01-01 12:00:02" ,"Male","25-34","High","Blog","0.001713");
        ImpressionRecord i2 = new ImpressionRecord(id_2,"2015-02-02 14:12:05" ,"Female","<25","Medium","Social Media","0.000001");
        ImpressionRecord i3 = new ImpressionRecord(id_3,"2015-02-02 14:13:05" ,"Female","35-44","Medium","Shopping","0.000000");
        ImpressionRecord i4 = new ImpressionRecord(id_4,"2015-02-02 14:14:05" ,"Female","45-54","Low","News","0.000000");
        ImpressionRecord i5 = new ImpressionRecord(id_5,"2015-02-02 14:14:05" ,"Male",">54","Medium","Social Media","0.000000");

        ServerRecord s1 = new ServerRecord(id_1,"2015-01-01 12:01:21","2015-01-01 12:05:13","7","No");
        ServerRecord s2 = new ServerRecord(id_2,"2015-02-02 14:12:05","2015-02-02 14:12:09","1","No");

        ClickRecord c1 = new ClickRecord(id_1,"2015-01-01 12:01:21","10.635709");
        ClickRecord c2 = new ClickRecord(id_2,"2015-02-02 14:12:05","11.416722");

        // Add records to map
        impressionRecordsMap.put(i1.getId(), i1);
        impressionRecordsMap.put(i2.getId(), i2);
        impressionRecordsMap.put(i3.getId(), i3);
        impressionRecordsMap.put(i4.getId(), i4);
        impressionRecordsMap.put(i5.getId(), i5);

        serverRecordsMap.put(s1.getId(), s1);
        serverRecordsMap.put(s2.getId(), s2);

        clickRecordsMap.put(c1.getId(), c1);
        clickRecordsMap.put(c2.getId(), c2);

        Records records = new Records(impressionRecordsMap, serverRecordsMap, clickRecordsMap);
        Metrics metrics = new Metrics(records);
        Campaign campaign = new Campaign("random", records, metrics, "definition");
        RecordsTest.model = new DashboardModel();
        model.setCurrentCampaign(campaign);
    }

    @Test
    @DisplayName("Test Full Filtering Process")
    public void fullFilterProcessTest() {
        Map<String, Collection<ImpressionRecord>> filteredMap1 = null;
        Map<String, Collection<ImpressionRecord>> filteredMap2 = null;
        try {
            System.out.println("FilterTree Root Data: " + model.getFilterTree().getRootData());
            System.out.println(model.getCurrentCampaign().getRecords().getAllRecords().keySet().size());
            filteredMap1 = model.getFilterTree().filter("age,<25");
            System.out.println(filteredMap1);
            System.out.println("FilterTree Root Data: " + model.getFilterTree().getRootData());
            System.out.println("FilterTree Data: " + model.getFilterTree().getCurrentData());
            //System.out.println("getRecords: " + model.getCurrentCampaign().getRecords().getAllRecords().entries());
            model.getCurrentCampaign().getRecords().update(filteredMap1); //TODO: THIS SOMEHOW CHANGES ROOT DATA
            System.out.println(filteredMap1); // TODO: filteredMap is losing keys as soon as it is passed into this method
            //System.out.println("getRecords: " + model.getCurrentCampaign().getRecords().getAllRecords().entries());
            System.out.println("FilterTree Root Data: " + model.getFilterTree().getRootData());
            System.out.println("FilterTree Data: " + model.getFilterTree().getCurrentData());
            int size1 = model.getFilterTree().getCurrentData().keySet().size();

            System.out.println("**************************************");

            System.out.println("FilterTree Root Data: " + model.getFilterTree().getRootData());
            System.out.println("FilterTree Data: " + model.getFilterTree().getCurrentData());

            filteredMap2 = model.getFilterTree().filter("age,25-34");
            System.out.println(filteredMap2);
            System.out.println("FilterTree Root Data: " + model.getFilterTree().getRootData());
            System.out.println("FilterTree Data: " + model.getFilterTree().getCurrentData());

            model.getCurrentCampaign().getRecords().update(filteredMap2);
            int size2 = model.getFilterTree().getCurrentData().keySet().size();
            System.out.println("FilterTree Root Data: " + model.getFilterTree().getRootData());
            System.out.println(size1 + ", " + size2);
        } catch (Exception e) {
            e.printStackTrace();
            //fail("Wrong exception thrown");
            //return;
        }
        System.out.println(filteredMap1);
        System.out.println(filteredMap2);

        /*assertTrue(filteredMap1.keySet().size() == 3
                && filteredMap2.keySet().size() == 1
                && filteredMap3.keySet().size() == 3);*/
    }
}
