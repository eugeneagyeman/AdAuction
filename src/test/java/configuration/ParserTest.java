package configuration;

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
    @DisplayName("Test Date Format")
    void DateFormatTest() {
        String dateTime = "2015-01-01 13:06:48";
        LocalDateTime parsedDateTime = Parser.parseDateTime(dateTime);
        LocalDateTime test = LocalDateTime.of(2015,1,1 , 13, 6, 48);
        assertEquals(test, parsedDateTime);
    }


}
