package com.capgemini.csd.tippkick.cukes.pages;

import com.capgemini.csd.tippkick.cukes.common.BrowserAccess;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserScoresPage extends AbstractPageObject {
    private static final String TABLE_SELECTOR = ".score-table";
    private static final int COL_USER_ID = 0;
    private static final int COL_SCORE = 1;

    private final String baseUrl;

    public UserScoresPage(BrowserAccess browserAccess, @Value("${tippwertung.ui.url:http://localhost:4200}") String uiUrl) {
        super(browserAccess);
        baseUrl = uiUrl;
    }

    public void goTo() {
        webDriver().navigate().to(baseUrl);
    }

    public List<UserScore> getDisplayedScores() {
        List<UserScore> scores = new ArrayList<>();
        List<WebElement> rows = tableRows(By.cssSelector(TABLE_SELECTOR));
        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            scores.add(new UserScore(Integer.valueOf(columns.get(COL_USER_ID).getText()),
                    Integer.valueOf(columns.get(COL_SCORE).getText())));
        }
        return scores;
    }


    @Getter
    @AllArgsConstructor
    public static class UserScore {
        private Integer userId;
        private Integer score;
    }
}
