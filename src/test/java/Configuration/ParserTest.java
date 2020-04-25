package Configuration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    //Test to assess system response when no file is found or given as an input
    @Test
    public void noFileInputTest() throws IOException {
        Parser p = new Parser();
        try{
            p.serverLogsParser("");
            fail("No exception thrown.");
        }catch(Exception ex){
            //assertTrue(ex.getMessage().contains("No file found for"));
        }
    }

    //Test to assess system response when an unexpected file is passed as input or incorrect values are parsed
    @Test
    public void incorrectFileInputTest() throws IOException {
        Parser p = new Parser();
        try{
            p.serverLogsParser("server_log.txt");
            fail("No exception thrown.");
        }catch(Exception ex){
            //assertTrue(ex.getMessage().contains("incorrectFileInput"));
        }
        try{
            p.serverLogsParser("gibberish.csv");
            fail("No exception thrown.");
        }catch(Exception ex){
            //assertTrue(ex.getMessage().contains("incorrectFileInput"));
        }
    }
    @Test
    void serverLogsParser() {
    }

    @Test
    void clickLogsParser() {
    }

    @Test
    void impressionLogsParser() {
    }
    @Test
    void DateFormatTest(){
        String date ="2015-01-01 13:06:48";
        Parser p =new Parser();
        LocalDateTime time=p.parseDateTime(date);
        LocalDateTime test= LocalDateTime.of(2015,1,1 , 13, 06, 48);
        assertEquals(test, time);
    }
    @Test
    void dateDifference() {
    }


}
