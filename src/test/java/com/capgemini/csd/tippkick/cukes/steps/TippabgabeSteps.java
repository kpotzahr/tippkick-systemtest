package com.capgemini.csd.tippkick.cukes.steps;

import com.capgemini.csd.tippkick.cukes.common.StepVariables;
import com.capgemini.csd.tippkick.cukes.steps.to.TippTestTO;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class TippabgabeSteps {
    private final TestRestTemplate tippabgabeRestTemplate;
    private final StepVariables stepVariables;

    private HttpStatus statusFromBet;

    @When("^Es werden folgende Tipps abgegeben:$")
    public void givenBets(List<TippTestTO> bets) throws InterruptedException {
        for (TippTestTO bet : bets) {
            createBet(bet.getTipper(), bet.getSpiel(), bet.getErgebnis());
        }
    }

    @When("^\"([^\"]*)\" gibt für \"([^\"]*)\" den Tipp \"([^\"]*)\" ab$")
    public void createBet(String user, String matchName, String result) throws InterruptedException {
        if ("TooLate".equals(user)) {
            // wait until game is started
            TimeUnit.SECONDS.sleep(8);
        }

        BetTO bet = BetTO.builder()
                .hometeamScore(Integer.valueOf(result.split(":")[0]))
                .foreignteamScore(Integer.valueOf(result.split(":")[1]))
                .ownerId(stepVariables.getUserId(user))
                .build();

        ResponseEntity<Void> response = tippabgabeRestTemplate.postForEntity("/tippabgabe/"
                + stepVariables.getMatchId(matchName), bet, Void.class);
        statusFromBet = response.getStatusCode();
    }

    @Then("^Der Tipp wird nicht angenommen$")
    public void betNotAccepted() {
        assertThat(statusFromBet).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class BetTO {
        private int foreignteamScore;
        private int hometeamScore;
        private Integer ownerId;
    }
}
