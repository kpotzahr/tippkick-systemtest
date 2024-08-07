package com.capgemini.csd.tippkick.cukes;

import com.capgemini.csd.tools.CucumberMetricPoster;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources",
        tags = {"not @skip"},
        glue = {"com.capgemini.csd.tippkick.cukes"},
        plugin = {"pretty", "html:build/cucumber-reports", "json:build/cucumber.json"}
)
public class RunCukesTest {
    @AfterClass
    public static void sendToInflux() throws IOException {
        new CucumberMetricPoster("build/cucumber.json").writeTestdataToInfluxDb(
                "http://localhost:8086", "tippkickcucumber", "tippkick");
    }
}
