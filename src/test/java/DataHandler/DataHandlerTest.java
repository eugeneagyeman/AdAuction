package DataHandler;

import Configuration.Configuration;
import Dashboard.DashboardModel;
import Dashboard.Filter;
import POJOs.*;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OutputCapturer {
    private PrintStream origOut;

    private ByteArrayOutputStream outputStream;

    public void start()
    {
        this.origOut = System.out;
        this.outputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(this.outputStream);
        System.setOut(ps);
    }

    public String getOutput() {
        System.out.flush();
        return this.outputStream.toString().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
    }

    public void stop() {
        System.setOut(this.origOut);
    }
}
class DataHandlerTest {
    OutputCapturer outputHarness;
    SecurityManager oldSM;

    @BeforeEach
    public void setupTest() {
        this.outputHarness = new OutputCapturer();
        this.outputHarness.start();
    }
    @AfterEach
    public void tearDown()
    {
        this.outputHarness.stop();
    }

    private static DashboardModel model;

    //System must compute the correct number of clicks
    @Test
    @DisplayName("Test Number of Clicks")
    public void ClicksTest() throws IOException {
        ClickRecord c1 = new ClickRecord("97065088426436600","2015-01-04 13:30:24","10.635709");
        ClickRecord c2= new ClickRecord("97065088426436600","2015-01-12 14:13:05","9.468576");
        assertEquals(c1.getClickCost(), 10.635709);
        assertEquals(c1.getClickCost(), 9.468576);
        Multimap<String, Record> iR = null;
        Multimap<String, Record> sR = null;
        Multimap<String, Record> cR = null;
        cR.put("97065088426436600",c1);
        cR.put("97065088426436600",c2);
        Records record=new Records(iR,sR,cR);
        Metrics metric=new Metrics(record);
        assertEquals(metric.calculateNumOfClicks(),2);

        model = new Configuration().buildDashboard();
        Metrics metric1=model.getMetrics();
        assertEquals(metric1.calculateNumOfClicks(),23924);
    }

    @Test
    @DisplayName("Test campaignUnique")
    public void UniquesTest() throws IOException {
        model = new Configuration().buildDashboard();
        Metrics metric =model.getMetrics();
        assertEquals(metric.calculateNumOfUniques(),533953);
    }

    @Test
    @DisplayName("Test NumberOfImpressions")
    public void NumberOfImpressionsTest() throws IOException {
        model = new Configuration().buildDashboard();
        Metrics metric =model.getMetrics();
        assertEquals(metric.getNumOfImpressions(),486105);
    }

    @Test
    @DisplayName("Test NumberOfConversions")
    public void NumberOfConversionsTest() throws IOException {
        model = new Configuration().buildDashboard();
        Metrics metric =model.getMetrics();
        assertEquals(metric.getNumOfConversions(),2026);
    }

    @Test
    @DisplayName("Test NumberofBounces")
    public void NumberofBouncesTest() throws IOException {
        model = new Configuration().buildDashboard();
        Metrics metric =model.getMetrics();
        assertEquals(metric.calculateNumOfBounces(),7006);
    }

    @Test
    @DisplayName("Test TotalCost")
    public void  TotalCostTest() throws IOException {
        model = new Configuration().buildDashboard();
        Metrics metric =model.getMetrics();
        assertEquals(metric.calculateTotalCost(),BigDecimal.valueOf(487.0565));
    }

    @Test
    @DisplayName("Test  average number of clicks per impression")
    public void  computeCTRTest() throws IOException {
        model = new Configuration().buildDashboard();
        Metrics metric =model.getMetrics();
        assertEquals(metric.calculateClickThroughRate(),BigDecimal.valueOf(4.92157044));
    }

    @Test
    @DisplayName("Test cost per click  ")
    public void  computeCPATest() throws IOException {
        model = new Configuration().buildDashboard();
        Metrics metric =model.getMetrics();
        assertEquals(metric.calculateCostPerAction(),BigDecimal.valueOf(0.24040301));
    }

    @Test
    @DisplayName("Test cost per auction")
    public void  computeCPCTest() throws IOException {
        model = new Configuration().buildDashboard();
        Metrics metric =model.getMetrics();
        assertEquals(metric.calculateCostPerClick(), BigDecimal.valueOf(4.916225629));
    }

    @Test
    @DisplayName("Test cost per thousand impressions  ")
    public void  computeCPMTest() throws IOException {
        model = new Configuration().buildDashboard();
        Metrics metric =model.getMetrics();
        assertEquals(metric.calculateCostPerThousand(),BigDecimal.valueOf(1.0019574));
    }

    @Test
    @DisplayName("Test  bounce rate")
    public void  calculateBounceRate() throws IOException {
        model = new Configuration().buildDashboard();
        Metrics metric =model.getMetrics();
        assertEquals(java.util.Optional.ofNullable(metric.calculateCostPerThousand()),29.2844);
    }
    @Test
    @DisplayName("Test AudienceSegmentsTest")
    public void  AudienceSegmentsTest() throws IOException {
        Configuration config=new Configuration();
        ImpressionRecord ir1=new ImpressionRecord(
                "4620864431353610000",
                "2015/1/1 12:00",
                "Male","25-34",
                "High","Blog",
                "0.001713");
        ImpressionRecord ir2=new ImpressionRecord(
                "3365479180556150000",
                " 2015/1/1 12:00",
                "Female","35-44",
                "Medium","News",
                "0.002762");
        ImpressionRecord ir4=new ImpressionRecord(
                "399593948382193000",
                "2015/1/1  12:00:10",
                "Male","<25",
                "Low","Social",
                "0.002064");
      Multimap<String, ImpressionRecord> impressionRecords = null;
      impressionRecords.put("97065088426436600",ir1);
      impressionRecords.put("3365479180556150000",ir2);
      impressionRecords.put("399593948382193000",ir4);
        Filter f= new Filter();
        try{
            f.impressionsByAge(impressionRecords);
            String out = outputHarness.getOutput();
            assertTrue(out.contains("Impressions <25: 1"+"Impressions 25-34: 1"+"Impressions <35-44: 1"+"Impressions <45-54: 0"
                    +"Impressions >54: 1"), "correct ageGroup");
        }catch(Exception ex){
        }
        try{
            f.impressionsByGender(impressionRecords);
            String out = outputHarness.getOutput();
            assertTrue(out.contains("Female impressions: 1"+"Male impressions: 2"), "correct genderGroup");
        }catch(Exception ex){
        }
    }




}