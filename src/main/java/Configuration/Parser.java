package Configuration;

import POJOs.ClickRecord;
import POJOs.ImpressionRecord;
import POJOs.Record;
import POJOs.ServerRecord;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Parser {

    private static Reader in;

    static Multimap<String, Record> serverLogsParser(String logCsv) throws IOException {
        Multimap<String, Record> serverLogs = ArrayListMultimap.create();
        in = new FileReader(logCsv);
        Iterable<CSVRecord> serverLogCSVRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        //TODO: Exception Handling for incorrect file input
        for (CSVRecord record : serverLogCSVRecords) {
            String entryDate = record.get("Entry Date");
            String id = record.get("ID");
            String exitDate = record.get("Exit Date");
            String pagesViewed = record.get("Pages Viewed");
            String conversion = record.get("Conversion");

            Record serverRecord = new ServerRecord(id, entryDate, exitDate, pagesViewed, conversion);
            serverLogs.put(id, serverRecord);

        }
        return serverLogs;

    }

    static Multimap<String, Record> clickLogsParser(String logCsv) throws IOException {
        Multimap<String, Record> clickMultiMap = ArrayListMultimap.create();
        in = new FileReader(logCsv);
        Iterable<CSVRecord> clickLogsCSVRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

        for (CSVRecord record : clickLogsCSVRecords) {
            String id = record.get("ID");
            String date = record.get("Date");
            String click_cost = record.get("Click Cost");

            Record clickRecord = new ClickRecord(id, date, click_cost);
            clickMultiMap.put(id, clickRecord);

        }
        return clickMultiMap;
    }

    static Multimap<String, Record> impressionLogsParser(String logCsv) throws IOException {
        Multimap<String, Record> impressionsLogs = ArrayListMultimap.create();

        in = new FileReader(logCsv);
        Iterable<CSVRecord> impressionsCSVRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

        for (CSVRecord record : impressionsCSVRecords) {
            String date = record.get("Date");
            String id = record.get("ID");
            String gender = record.get("Gender");
            String age = record.get("Age");
            String income = record.get("Income");
            String context = record.get("Context");
            String impression_cost = record.get("Impression Cost");

            Record impressionRecord = new ImpressionRecord(id, date, gender, age, income, context, impression_cost);
            impressionsLogs.put(id, impressionRecord);

        }
        return impressionsLogs;
    }

    public static LocalDateTime parseDateTime(String dateTime) {
        // YYYY/MM/DD HH:MM:SS -- According to requirements document
        // YYYY-MM-DD HH:MM:SS -- According to test data (Using this)
        DateTimeFormatter dfs = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))
                .appendOptional(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .toFormatter();

        if (dateTime.contains("n/a")) return null;
        if (dateTime.contains("T")) return LocalDateTime.parse(dateTime, dfs);
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

    public static Long dateDifference(LocalDateTime entry, LocalDateTime exit) {
        // Edge cases eg new day, month, or year, daylight saving time
        long time = 0;

        if (!exit.isEqual(LocalDateTime.MAX)) {
            Duration difference = Duration.between(entry, exit);
            time = Math.abs(difference.toSeconds());
        } else
            return null;

        return time;
    }

    public class IncorrectFileException extends Exception {
        IncorrectFileException(String message) {
            super(message);
        }
    }
}
