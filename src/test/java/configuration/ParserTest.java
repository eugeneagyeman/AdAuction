package configuration;

import POJOs.ClickRecord;
import POJOs.Record;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    //Test to assess system response when no file is found or given as an input
    @Test
    @DisplayName("Test no File Input")
    void noFileInputTest() {
        try {
            Parser.serverLogsParser("");
            fail("No exception thrown.");
        } catch (Exception e){
            assertTrue(true);
        }
    }

    //Test to assess system response when an unexpected file is passed as input or incorrect values are parsed
    @ParameterizedTest
    @ValueSource(strings = {"server_log.txt", "gibberish.csv"})
    @DisplayName("Test Incorrect File Input")
    void incorrectFileInputTest(String input) {
        try {
            Parser.serverLogsParser(input);
            fail("No exception thrown.");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("Test Server Log Parser")
    void serverLogsParserTest() {
        try {
            Parser.serverLogsParser("TestData/server_log.csv");
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    @DisplayName("Test Click Log Parser")
    void clickLogsParserTest() {
        try {
            Parser.clickLogsParser("TestData/click_log.csv");
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    @DisplayName("Test Impression Log Parser")
    void impressionLogsParserTest() {
        try {
            Parser.impressionLogsParser("TestData/impression_log.csv");
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    @DisplayName("Test how Click Log Parser Deals with Negative Costs")
    void negClickCostParserTest() {
        try {
            Multimap<String, ClickRecord> map = ArrayListMultimap.create();
            Multimap m = Parser.clickLogsParser("TestData/click_log_neg.csv");
            map = Multimaps.filterValues(m, v -> v instanceof ClickRecord);
            for (ClickRecord record : map.values()) {
                if (!record.getClickCost().equals("0"))
                    fail("Click costs below 0 should default to 0"); //TODO: Fix this
            }
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    @DisplayName("Test Date Format")
    void DateFormatTest() {
        String dateTime = "2015-01-01 13:06:48";
        LocalDateTime parsedDateTime = Parser.parseDateTime(dateTime);
        LocalDateTime test = LocalDateTime.of(2015,1,1 , 13, 6, 48);
        assertEquals(test, parsedDateTime);
    }

    @Test
    void dateDifference() {
    }

}
