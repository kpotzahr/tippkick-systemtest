package com.capgemini.csd.tippkick.cukes.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

@Component
public class BrowserAccess {
    private static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";
    private WebDriver webDriver;

    void startBrowser() {
        if (null == webDriver) {
            String property = System.getProperty(WEBDRIVER_CHROME_DRIVER);
            if (null == property) {
                System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
            }
            webDriver = new ChromeDriver();
        }
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    void shutDown() {
        if (null != webDriver) {
            webDriver.close();
            webDriver = null;
        }
    }


}
