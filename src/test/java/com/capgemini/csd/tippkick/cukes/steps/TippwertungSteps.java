package com.capgemini.csd.tippkick.cukes.steps;

import com.capgemini.csd.tippkick.cukes.common.StepVariables;
import com.capgemini.csd.tippkick.cukes.pages.UserScoresPage;
import com.capgemini.csd.tippkick.cukes.steps.to.UserScoreTestTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class TippwertungSteps {
    private final TestRestTemplate tippwertungRestTemplate;
    private final StepVariables stepVariables;
    private final UserScoresPage userScoresPage;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Then("^Es wird folgende Rangliste berechnet:$")
    public void checkScores(List<UserScoreTestTO> expectedScores) throws IOException {
        ResponseEntity<String> response = tippwertungRestTemplate.getForEntity(
                "/score", String.class);
        List<UserScoreTestTO> acualUserScores = objectMapper.readValue(response.getBody(),
                new TypeReference<List<UserScoreTestTO>>() {
                });

        Map<Integer, Integer> userId2score = new HashMap<>();
        for (UserScoreTestTO score : acualUserScores) {
            userId2score.put(score.getUserId(), score.getScore());
        }

        for (UserScoreTestTO expectedScore : expectedScores) {
            Integer actualScore = userId2score.get(stepVariables.getUserId(expectedScore.getTipper()));
            assertThat(actualScore).isEqualTo(expectedScore.getScore());
        }
    }

    @Then("^Es wird folgende Rangliste angezeigt:$")
    public void checkScoresInUi(List<UserScoreTestTO> expectedScores) {
        userScoresPage.goTo();
        List<UserScoresPage.UserScore> displayedScores = userScoresPage.getDisplayedScores();
        assertThat(displayedScores).hasSameSizeAs(expectedScores);
        for (int i = 0; i < expectedScores.size(); i++) {
            UserScoreTestTO expectedScore = expectedScores.get(i);
            UserScoresPage.UserScore actualScore = displayedScores.get(i);
            assertThat(actualScore.getScore()).isEqualTo(expectedScore.getScore());
            assertThat(actualScore.getUserId()).isEqualTo(stepVariables.getUserId(expectedScore.getTipper()));
        }
    }
}