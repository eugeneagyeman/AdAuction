package DataHandler;

import DataHandler.pojos.ClickRecord;
import DataHandler.pojos.ImpressionRecord;
import DataHandler.pojos.Record;
import DataHandler.pojos.ServerRecord;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;

class Parser {
    private static final String TEST_DATA_CLICK_LOG_CSV = "TestData/click_log.csv";
    private static final String TEST_DATA_SERVER_LOG_CSV = "TestData/server_log.csv";
    private static final String TEST_DATA_IMPRESSION_LOG_CSV = "TestData/impression_log.csv";
    private static Reader in;

    static Multimap<String, Record> serverLogsParser() throws IOException {
        Multimap<String, Record> serverLogs = ArrayListMultimap.create();
        in = new FileReader(TEST_DATA_SERVER_LOG_CSV);
        Iterable<CSVRecord> serverLogCSVRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

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

    static Multimap<String, Record> clickLogsParser() throws IOException {
        Multimap<String, Record> clickMultiMap = ArrayListMultimap.create();
        in = new FileReader(TEST_DATA_CLICK_LOG_CSV);
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

    static Multimap<String, Record> impressionLogsParser() throws IOException {
        Multimap<String, Record> impressionsLogs = ArrayListMultimap.create();

        in = new FileReader(TEST_DATA_IMPRESSION_LOG_CSV);
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


}
