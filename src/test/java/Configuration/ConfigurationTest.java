package Configuration;
import Dashboard.DashboardModel;
import POJOs.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ConfigurationTest {
    private static DashboardModel model;

    //System must receive three files (server, impression_log, click_log) before it can begin processing.
    @Test
    @DisplayName("Test threeFilesCheck")
    void threeFilesCheck(){
        User user =new User("A212","123");
        String serverLogFilePath = "TestData/server_log.csv", impressionsLogFilePath =  "TestData/impression_log.csv", clickLogFilePath = null;
        try{
            Configuration config = new Configuration(user,serverLogFilePath,impressionsLogFilePath,clickLogFilePath);

        }catch(Exception ex){
        }
        fail("No exception thrown.");
    }

    @Test
    @DisplayName("Test campaignUniqueTest")
    void campaignUniqueTest(){
        User user =new User("David","123");
        final String TEST_DATA_CLICK_LOG_CSV = "TestData/click_log.csv";
        final String TEST_DATA_SERVER_LOG_CSV = "TestData/server_log.csv";
        final String TEST_DATA_IMPRESSION_LOG_CSV = "TestData/impression_log.csv";
        try {
            Configuration config = new Configuration(user,TEST_DATA_SERVER_LOG_CSV,TEST_DATA_IMPRESSION_LOG_CSV,TEST_DATA_CLICK_LOG_CSV);
            config.buildDashboard().getCurrentCampaign();
        } catch (Exception e){
        }
    }


}
