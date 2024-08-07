package com.capgemini.csd.tippkick.cukes.common;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
public class StepVariables {
    private static final Map<String, Integer> USERNAME2ID = ImmutableMap.of(
            "Ronaldo", 1, "KnowItAll", 2, "TooLate", 3
    );

    private Map<String, Integer> matchname2id = new HashMap<>();

    public void addMatch(String matchname, Integer matchId) {
        matchname2id.put(matchname, matchId);
    }

    public Integer getUserId(String username) {
        return USERNAME2ID.get(username);
    }

    public Integer getMatchId(String matchname) {
        return matchname2id.get(matchname);
    }


    void clear() {
        matchname2id.clear();
    }
}
