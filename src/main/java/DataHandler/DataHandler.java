package DataHandler;

import DataHandler.pojos.ClickRecord;
import DataHandler.pojos.ImpressionRecord;
import DataHandler.pojos.Record;
import DataHandler.pojos.ServerRecord;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Collection;

import static DataHandler.Parser.*;
import static DataHandler.Parser.clickLogsParser;
import static DataHandler.Parser.serverLogsParser;

public class DataHandler {
    private static final DataStorage storage = new DataStorage();
    private static Multimap<String, ImpressionRecord> impressionRecords;
    private static Multimap<String, ClickRecord> clickRecords;
    private static Multimap<String, ServerRecord> serverRecords;

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

            System.out.println("Total Cost of Campaign: " + totalCostofCampaigninPence());
            System.out.println("CPM: " + costPerThousandImpressions());
            System.out.println("Number of Conversions: " + numberOfConversions());
            System.out.println("Cost Per Acquisition: " + costPerAcquisition());
            System.out.println("Cost Per Click: " + costPerClick());
            System.out.println("Number of Bounces: " + bounceCount());

            /*serverLogsCount();
            clickCount();
            impressionsCount();
            impressionsByGender();
            impressionsByAge();*/

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

    private int clickCount() {

        int size = clickRecords.entries().size();
        System.out.println("Total Click Logs: " + size);
        return size;
    }

    private int impressionsCount() {
        int size = impressionRecords.entries().size();
        System.out.println("Total Impression Records: " + size);
        return size;

    }

    private void impressionsByGender() {
        int impressionsMale = 0;
        for (Record value : impressionRecords.values()) {
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
        int impressionsBelow25 = 0;
        int impressions25To34 = 0;
        int impressions35To44 = 0;
        int impressions45To54 = 0;
        int impressionsAbove54 = 0;
        for (Object value : impressionRecords.values()) {
            String stringValue = value.toString();
            if (stringValue.contains("<25"))
                impressionsBelow25++;
            else if (stringValue.contains("25-34"))
                impressions25To34++;
            else if (stringValue.contains("35-44"))
                impressions35To44++;
            else if (stringValue.contains("45-54"))
                impressions45To54++;
            else if (stringValue.contains(">54"))
                impressionsAbove54++;
        }
        System.out.println("Impressions <25: " + impressionsBelow25);
        System.out.println("Impressions 25-34: " + impressions25To34);
        System.out.println("Impressions <35-44: " + impressions35To44);
        System.out.println("Impressions <45-54: " + impressions45To54);
        System.out.println("Impressions >54: " + impressionsAbove54);
    }

    //TODO: Ask about pounds vs pence
    private Float totalCostofCampaigninPence() {
        Float totalCost = impressionRecords.values()
                .stream()
                .map(ImpressionRecord::getImpressionCost)
                .reduce((float) 0.00, Float::sum);
        return totalCost;
    }

    private Double costOfCampaignInPounds(Float costInPence) {
        return (double) (costInPence / 100);
    }

    private Double costPerThousandImpressions() {
        //Loop through every impression
        int numOfImpressions = impressionsCount();
        float totalCost = totalCostofCampaigninPence();
        return (double) ((1000 * (totalCost)) / numOfImpressions);

    }

    private int numberOfConversions() {
        int conversions = (int) serverRecords.values()
                .stream()
                .map(ServerRecord::getConversion)
                .filter(response -> response.equalsIgnoreCase("yes"))
                .count();
        return conversions;
    }

    private Double costPerAcquisition() {
        Double cost = (double) (totalCostofCampaigninPence() / numberOfConversions());
        return cost;
    }

    private Double costPerClick() {
        return (Double) (double) (totalCostofCampaigninPence() / clickCount());
    }

    // Get individual server records
    // Extract dates and calculate difference as an int
    // Implement pre-defined logic to check for bounce
    private int bounceCount() {
        int count = 0;
        Collection<ServerRecord> records = serverRecords.values();
        for (ServerRecord record : records) {
            String entryDate = record.getEntryDate();
            String exitDate = record.getExitDate();
            String conversion = record.getConversion().replaceAll("\\s","");
            int pagesViewed = Integer.parseInt(record.getPagesViewed().replaceAll("\\s",""));
            Long time = dateDifference(entryDate, exitDate);

            // Bounce logic
            if (time != null) {
                if (conversion.equals("No") && pagesViewed <= 1 && time <= 10)
                    count++;
            }
        }

        return count;
    }

    private Long dateDifference(String entry, String exit) {
        // Edge cases eg new day, month, or year, daylight saving time
        long time = 0;

        if (!exit.replaceAll("\\s","").equals("n/a")) {
            LocalDateTime entryDateTime = parseDateTime(entry);
            LocalDateTime exitDateTime = parseDateTime(exit);
            Duration difference = Duration.between(entryDateTime, exitDateTime);
            time = Math.abs(difference.toSeconds());
        } else
            return null;

        return time;
    }

    private LocalDateTime parseDateTime(String dateTime) {
        // YYYY/MM/DD HH:MM:SS -- According to requirements document
        // YYYY-MM-DD HH:MM:SS -- According to test data (Using this)

        // Split datetime string into individual components
        String[] split = dateTime.split(" ");
        String[] splitDate = split[0].split("-");
        String[] splitTime = split[1].split(":");

        // Convert string values to int
        Integer[] splitDateInt = new Integer[splitDate.length];
        for (int i = 0; i < splitDate.length; i++)
            splitDateInt[i] = Integer.parseInt(splitDate[i]);

        Integer[] splitTimeInt = new Integer[splitTime.length];
        for (int i = 0; i < splitTime.length; i++)
            splitTimeInt[i] = Integer.parseInt(splitTime[i]);

        // Convert string into java.time object
        LocalDate localDate = LocalDate.of(splitDateInt[0], splitDateInt[1], splitDateInt[2]);
        LocalTime localTime = LocalTime.of(splitTimeInt[0], splitTimeInt[1], splitTimeInt[2]);

        return LocalDateTime.of(localDate, localTime);
    }
}


