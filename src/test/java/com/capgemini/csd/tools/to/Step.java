package com.capgemini.csd.tools.to;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Step {
    private String name;
    private StepResult result;

    long getDurationMs() {
        return (null == result) ? 0 : result.getDurationInMs();
    }
}
