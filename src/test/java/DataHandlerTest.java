import POJOs.ClickRecord;
import POJOs.ImpressionRecord;
import POJOs.Record;
import POJOs.Records;
import com.google.common.collect.Multimap;
import configuration.Configuration;
import dashboard.DashboardModel;
import dashboard.Filter;
import outputCapturer.OutputCapturer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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




}
