package com.capgemini.csd.tools.to;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class StepResult {
    private static final String PASSED = "passed";
    private static final String FAILED = "failed";
    private static final String SKIPPED = "skipped";
    private static final String UNDEFINED = "undefined";

    private long duration;
    private String status;

    ScenarioResult getResult() {
        switch (status) {
            case PASSED:
                return ScenarioResult.PASSED;
            case FAILED:
                return ScenarioResult.FAILED;
            default:
                return ScenarioResult.SKIPPED;
        }
    }

    long getDurationInMs() {
        return duration / 1_000_000;
    }
}
