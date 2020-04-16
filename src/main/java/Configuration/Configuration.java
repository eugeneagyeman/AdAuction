package configuration;

import dashboard.DashboardModel;
import metrics.Metrics;
import POJOs.*;
import com.google.common.collect.Multimap;

import java.io.IOException;

public class Configuration {
    private final String TEST_DATA_CLICK_LOG_CSV = "TestData/click_log.csv";
    private final String TEST_DATA_SERVER_LOG_CSV = "TestData/server_log.csv";
    private final String TEST_DATA_IMPRESSION_LOG_CSV = "TestData/impression_log.csv";
    private User user;
    private String serverLogFilePath, impressionsLogFilePath, clickLogFilePath;

    public Configuration(User user, String serverLogFilePath, String impressionsLogFilePath, String clickLogFilePath) {
        this.user = user;
        this.serverLogFilePath = serverLogFilePath;
        this.impressionsLogFilePath = impressionsLogFilePath;
        this.clickLogFilePath = clickLogFilePath;

        try {
            buildDashboard(impressionsLogFilePath, serverLogFilePath, clickLogFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration() {
        serverLogFilePath = TEST_DATA_SERVER_LOG_CSV;
        impressionsLogFilePath = TEST_DATA_IMPRESSION_LOG_CSV;
        clickLogFilePath = TEST_DATA_CLICK_LOG_CSV;
        try {
            buildDashboard(impressionsLogFilePath, serverLogFilePath, clickLogFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DashboardModel buildDashboard(String impressionsLogFilePath, String serverLogFilePath, String clickLogFilePath) throws IOException {
        Multimap<String, Record> impressionRecordmap = Parser.impressionLogsParser(impressionsLogFilePath);
        Multimap<String, Record> serverRecordmap = Parser.serverLogsParser(serverLogFilePath);
        Multimap<String, Record> clickRecordmap = Parser.clickLogsParser(clickLogFilePath);

        Records records = new Records(impressionRecordmap, serverRecordmap, clickRecordmap);
        Metrics metrics = new Metrics(records);
        Campaign campaign = new Campaign("random", records, metrics, "definition");
        DashboardModel dashboardModel = new DashboardModel().setCurrentCampaign(campaign);
        dashboardModel.getListOfCampaigns().add(campaign);
        return dashboardModel;

    }

    public DashboardModel buildDashboard() throws IOException {
        return buildDashboard(impressionsLogFilePath, serverLogFilePath, clickLogFilePath);
    }


}
