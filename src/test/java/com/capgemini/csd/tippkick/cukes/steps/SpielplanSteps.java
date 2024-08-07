package com.capgemini.csd.tippkick.cukes.steps;

import com.capgemini.csd.tippkick.cukes.common.StepVariables;
import com.capgemini.csd.tippkick.cukes.steps.to.MatchTestTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class SpielplanSteps {
    private final TestRestTemplate spielplanRestTemplate;
    private final StepVariables stepVariables;

    @Given("^Spielplan:$")
    public void spielplan(List<MatchTestTO> matches) throws IOException {
        TournamentTO tournament = new TournamentTO();

        for (MatchTestTO match : matches) {
            tournament.getMatches().add(MatchTO.builder()
                    .hometeam(match.getHometeam())
                    .foreignteam(match.getForeignteam())
                    .starttime(match.getStartTime())
                    .build()
            );
        }
        ResponseEntity<Void> response = spielplanRestTemplate.postForEntity(
                "/turnierdaten", tournament, Void.class);
        log.info("Tournament Response: " + response.getBody() + " " + response.getStatusCodeValue());
        storeIds();
    }

    private void storeIds() throws IOException {
        ResponseEntity<String> response = spielplanRestTemplate.getForEntity("/spielplan", String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<MatchTO> matches = objectMapper.readValue(
                response.getBody(), new TypeReference<List<MatchTO>>() {
                });
        matches.forEach(m -> stepVariables.addMatch(getMatchname(m), m.getMatchId()));
    }

    private String getMatchname(MatchTO match) {
        return MatchTestTO.COUNTRY_SHORT2LONG_NAMES.get(match.hometeam)
                + "-" + MatchTestTO.COUNTRY_SHORT2LONG_NAMES.get(match.foreignteam);
    }

    @When("^Spiel \"([^\"]*)\" endet \"([^\"]*)\"$")
    public void matchFinalized(String match, String resultStr) throws InterruptedException {
        spielplanRestTemplate.postForEntity(
                "/admin/start/" + stepVariables.getMatchId(match), new HashMap<>(), Void.class);
        TimeUnit.SECONDS.sleep(1);
        Result result = Result.builder()
                .hometeamScore(Integer.valueOf(resultStr.split(":")[0]))
                .foreignteamScore(Integer.valueOf(resultStr.split(":")[1]))
                .build();
        spielplanRestTemplate.postForEntity(
                "/ergebnis/" + stepVariables.getMatchId(match), result, Void.class);
        TimeUnit.SECONDS.sleep(1);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MatchTO {
        private String starttime;
        private String hometeam;
        private String foreignteam;
        private int matchId;

    }

    @Getter
    @Setter
    private static class TournamentTO {
        private List<MatchTO> matches = new ArrayList<>();
    }

    @Getter
    @Setter
    @Builder
    private static class Result {
        private int foreignteamScore;
        private int hometeamScore;
    }
}
