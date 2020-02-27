package DataHandler;

import DataHandler.pojos.ImpressionRecord;
import DataHandler.pojos.Record;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.io.IOException;
import java.util.Collections;

import static DataHandler.Parser.*;
import static DataHandler.Parser.clickLogsParser;
import static DataHandler.Parser.serverLogsParser;

public class DataHandler {
    private static final DataStorage storage = new DataStorage();
    private static Multimap impressionRecords;
    private static Multimap clickRecords;
    private static Multimap serverRecords;

    public DataHandler() {
        try {

            System.out.println("Parsing Impression Logs...");
            Multimap<String, Record> impressionMap = impressionLogsParser();

            System.out.println("Parsing Click Logs...");
            Multimap<String, Record> clickMap = clickLogsParser();

            System.out.println("Parsing Server Logs...");
            Multimap<String, Record> serverMap = serverLogsParser();

            System.out.println("Storing in Memory");
            storeRecords(impressionMap);
            storeRecords(clickMap);
            storeRecords(serverMap);

            System.out.println("Done");
            impressionRecords = DataStorage.getImpressionRecords();
            clickRecords = DataStorage.getClickRecords();
            serverRecords = DataStorage.getServerRecords();
            serverLogsCount();
            clickCount();
            impressionsCount();
            impressionsByGender();
            impressionsByAge();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeRecords(Multimap<String, Record> records) {
        storage.getStorage().putAll(records);
    }

    private void serverLogsCount() {

        int size = serverRecords.entries().size();
        System.out.println("Total Server Records: " + size);
    }

    private void clickCount() {

        int size = clickRecords.entries().size();
        System.out.println("Total Click Logs: " + size);
    }

    private void impressionsCount() {
        int size = impressionRecords.entries().size();
        System.out.println("Total Impression Records: " + size);

    }

    private void impressionsByGender() {
        int impressionsMale = 0;
        for (Object value : impressionRecords.values()) {
            String stringValue = value.toString();
            if (stringValue.contains("Male"))
                    impressionsMale++;
        }
        int impressionsFemale = 0;
        for (Object value : impressionRecords.values()) {
            String stringValue = value.toString();
            if (stringValue.contains("Female"))
                impressionsFemale++;
        }
        System.out.println("Male impressions: " + impressionsMale);
        System.out.println("Female impressions: " + impressionsFemale);
    }

    private void impressionsByAge() {
        int impressions1 = 0;
        int impressions2 = 0;
        int impressions3 = 0;
        int impressions4 = 0;
        int impressions5 = 0;
        for (Object value : impressionRecords.values()) {
            String stringValue = value.toString();
            if (stringValue.contains("<25"))
                impressions1 ++;
            else if (stringValue.contains("25-34"))
                impressions2 ++;
            else if (stringValue.contains("35-44"))
                impressions3 ++;
            else if (stringValue.contains("45-54"))
                impressions4 ++;
            else if (stringValue.contains(">54"))
                impressions5 ++;
        }
        System.out.println("Impressions <25: " + impressions1);
        System.out.println("Impressions 25-34: " + impressions2);
        System.out.println("Impressions <35-44: " + impressions3);
        System.out.println("Impressions <45-54: " + impressions4);
        System.out.println("Impressions >54: " + impressions5);
    }
}
