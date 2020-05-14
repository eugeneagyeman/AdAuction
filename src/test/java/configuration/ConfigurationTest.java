package configuration;

import POJOs.*;

import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.junit.jupiter.api.*;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationTest {

    //System must receive three files (server, impression_log, click_log) before it can begin processing.
    @Test
    @DisplayName("Test Three Files Check")
    void threeFilesCheckTest() throws UnsupportedEncodingException {
        String a="ps";
        User user = new User("A212", a.getBytes("UTF-8"), "user");
        String serverLogFilePath = "TestData/server_log.csv", impressionsLogFilePath = "TestData/impression_log.csv";
        try {
            new Configuration(user, serverLogFilePath, impressionsLogFilePath, null);
            fail("No exception thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    // Tests to see whether the system will recognise a campaign as such
    @Test
    @DisplayName("Test Unique Campaign")
    void campaignUniqueTest() throws UnsupportedEncodingException {
        String a="ps";
        User user = new User("David",a.getBytes("UTF-8"), "user");
        final String SERVER_LOG_CSV = "TestData/server_log.csv";
        final String IMPRESSION_LOG_CSV = "TestData/impression_log.csv";
        final String CLICK_LOG_CSV = "TestData/click_log.csv";
        try {
            Configuration config = new Configuration(user,SERVER_LOG_CSV,IMPRESSION_LOG_CSV,CLICK_LOG_CSV);
            Campaign campaign = config.buildDashboard().getCurrentCampaign();
            assertNotNull(campaign);
        } catch (Exception e) {
            fail("Campaign has not been successfully built");

        }
    }

}

