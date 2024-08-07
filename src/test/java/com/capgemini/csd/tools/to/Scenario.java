package com.capgemini.csd.tools.to;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
class Scenario {
    private String id;
    private String name;
    private String type;
    private List<Tag> tags;
    private List<Step> steps;
    private List<Step> before;
    private List<Step> after;


    boolean isBackground() {
        return "background".equals(type);
    }

    boolean isUiTest() {
        return null != tags && tags.contains(Tag.UI_TAG);
    }

    void merge(Scenario background) {
        if (null != background.tags) {
            if (null == tags) {
                tags = new ArrayList<>();
            }
            tags.addAll(background.tags);
        }
        if (null != background.steps) {
            if (null == steps) {
                steps = new ArrayList<>();
            }
            steps.addAll(background.steps);
        }
        if (null != background.before) {
            if (null == before) {
                before = new ArrayList<>();
            }
            before.addAll(background.before);
        }
        if (null != background.after) {
            if (null == after) {
                after = new ArrayList<>();
            }
            after.addAll(background.after);
        }
    }

    ScenarioResult getResult() {
        if ((null != tags && tags.contains(Tag.SKIP_TAG)) || null == steps) {
            return ScenarioResult.SKIPPED;
        }
        Set<ScenarioResult> results = steps.stream().map(step -> step.getResult().getResult()).collect(Collectors.toSet());
        if (results.contains(ScenarioResult.FAILED)) {
            return ScenarioResult.FAILED;
        }
        if (results.contains(ScenarioResult.SKIPPED)) {
            return ScenarioResult.SKIPPED;
        }
        return ScenarioResult.PASSED;
    }

    long getDurationMs() {
        long durationMs = 0;
        if (null != before) {
            durationMs += before.stream().mapToLong(Step::getDurationMs).sum();
        }
        if (null != after) {
            durationMs += after.stream().mapToLong(Step::getDurationMs).sum();
        }
        if (null != steps) {
            durationMs += steps.stream().mapToLong(Step::getDurationMs).sum();
        }
        return durationMs;
    }


}
