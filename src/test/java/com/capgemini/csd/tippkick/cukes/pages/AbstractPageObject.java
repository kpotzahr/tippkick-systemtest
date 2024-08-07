package com.capgemini.csd.tippkick.cukes.pages;

import com.capgemini.csd.tippkick.cukes.common.BrowserAccess;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

@RequiredArgsConstructor
class AbstractPageObject {
    private final BrowserAccess browserAccess;

    WebDriver webDriver() {
        return browserAccess.getWebDriver();
    }

    List<WebElement> tableRows(By by) {
        return webDriver().findElement(by).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
    }
}
