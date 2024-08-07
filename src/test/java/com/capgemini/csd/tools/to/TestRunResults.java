package com.capgemini.csd.tools.to;

import java.util.ArrayList;
import java.util.List;

public class TestRunResults {
    private List<Scenario> scenarios;

    public TestRunResults(List<Feature> features) {
        scenarios = new ArrayList<>();
        features.forEach(f -> scenarios.addAll(f.getScenarios()));
    }

    private List<Scenario> scenarios() {
        return scenarios;
    }

    public long getNumberOfTests() {
        return scenarios().size();
    }

    public long getNumberOfUiTests() {
        return scenarios().stream().filter(Scenario::isUiTest).count();
    }

    public long getNumberOfApiTests() {
        return scenarios().stream().filter(s -> !s.isUiTest()).count();
    }

    public long getNumberOfSuccessfulTests() {
        return scenarios().stream().filter(s -> ScenarioResult.PASSED.equals(s.getResult())).count();
    }

    public long getNumberOfFailedTests() {
        return scenarios().stream().filter(s -> ScenarioResult.FAILED.equals(s.getResult())).count();
    }

    public long getNumberOfSkippedTests() {
        return scenarios().stream().filter(s -> ScenarioResult.SKIPPED.equals(s.getResult())).count();
    }


    public long getDuration() {
        return scenarios.stream().mapToLong(Scenario::getDurationMs).sum();
    }

    public long getDurationApiTests() {
        return scenarios.stream().mapToLong(s -> s.isUiTest() ? 0: s.getDurationMs()).sum();
    }

    public long getDurationUiTests() {
        return scenarios.stream().mapToLong(s -> s.isUiTest() ? s.getDurationMs() : 0).sum();
    }

}
