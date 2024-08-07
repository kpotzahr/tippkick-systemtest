package com.capgemini.csd.tippkick.cukes.steps.to;

import com.google.common.collect.ImmutableMap;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class MatchTestTO {
    private final static DateTimeFormatter START_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final Map<String, String> COUNTRY_LONG2SHORT_NAMES = ImmutableMap.of(
            "Deutschland", "GER", "Brasilien", "BRA");

    public static final Map<String, String> COUNTRY_SHORT2LONG_NAMES = ImmutableMap.of(
            "GER","Deutschland", "BRA",  "Brasilien");

    private String spiel;
    private String startzeit;

    public String getStartTime() {
        ZonedDateTime time = Instant.now().atZone(ZoneId.of("UTC"));
        if ("<sofort>".equals(startzeit)) {
            time = time.plusSeconds(5);
        } else if ("<nachher>".equals(startzeit)) {
            time = time.plusHours(3);
        }

        return time.format(START_TIME_FORMAT);
    }

    public String getHometeam() {
        return  COUNTRY_LONG2SHORT_NAMES.get(spiel.split("-")[0]);
    }

    public String getForeignteam() {
        return  COUNTRY_LONG2SHORT_NAMES.get(spiel.split("-")[1]);
    }
}
