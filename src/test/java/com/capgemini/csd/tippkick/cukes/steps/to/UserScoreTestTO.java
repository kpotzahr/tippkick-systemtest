package com.capgemini.csd.tippkick.cukes.steps.to;

import lombok.Data;

@Data
public class UserScoreTestTO {
    private String tipper;
    private int score;
    private int userId;
}
