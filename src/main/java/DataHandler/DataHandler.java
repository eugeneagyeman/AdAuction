package DataHandler;

import DataHandler.pojos.Record;
import com.google.common.collect.Multimap;

import java.io.IOException;

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
}
