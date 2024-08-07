package com.capgemini.csd.tippkick.cukes.common;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.test.context.ContextConfiguration;

import java.sql.SQLException;

@RequiredArgsConstructor
@ContextConfiguration(classes = {CucumberConfiguration.class})
public class CommonHooks {
    private static boolean isStarted = false;

    private final DbAccess dbAccess;
    private final StepVariables stepVariables;
    private final BrowserAccess browserAccess;

    @Before("@cleanData")
    public void cleanupData() throws SQLException {
        dbAccess.cleanupData();
    }

    @Before
    public void init() {
        stepVariables.clear();
    }

    @Before("@ui")
    public void openBrowser() {
        browserAccess.startBrowser();
    }

    @After("@ui")
    public void afterScenarioUI(Scenario scenario) {
        if (scenario.isFailed() && null != browserAccess.getWebDriver()) {
            byte[] screenshot = ((TakesScreenshot) browserAccess.getWebDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
        }
        browserAccess.shutDown();
    }

}
