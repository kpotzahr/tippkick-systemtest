package com.capgemini.csd.tools.to;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Feature {
    private String name;
    private List<Tag> tags;
    private List<Scenario> elements;

    List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        Scenario background = null;
        for (Scenario element : elements) {
            if (element.isBackground()) {
                background = element;
            } else {
                if (null != background) {
                    element.merge(background);
                }
                scenarios.add(element);
                background = null;
            }
        }
        return scenarios;
    }

}
