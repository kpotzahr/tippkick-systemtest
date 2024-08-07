package com.capgemini.csd.tools;

import com.capgemini.csd.tools.to.Feature;
import com.capgemini.csd.tools.to.TestRunResults;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
class CucumberMetricReader {
    private static final String METRIC_RUNTIME = "cucumber_runtime";
    private static final String METRIC_RUNTIME_UI = "cucumber_runtime_ui";
    private static final String METRIC_RUNTIME_API = "cucumber_runtime_api";

    private static final String METRIC_COUNT_TESTS = "cucumber_count_tests";
    private static final String METRIC_COUNT_TESTS_UI = "cucumber_count_tests_ui";
    private static final String METRIC_COUNT_TESTS_API = "cucumber_count_tests_api";
    private static final String METRIC_COUNT_TESTS_SUCCESSFUL = "cucumber_count_tests_successful";
    private static final String METRIC_COUNT_TESTS_FAILED = "cucumber_count_tests_failed";
    private static final String METRIC_COUNT_TESTS_SKIPPED = "cucumber_count_tests_skipped";

    private static final int PERCENT_FACTOR = 100;
    private static final String METRIC_PERCENTAGE_UI = "cucumber_percentage_ui";
    private static final String METRIC_PERCENTAGE_API = "cucumber_percentage_api";
    private static final String METRIC_PERCENTAGE_SUCCESSFUL = "cucumber_percentage_successful";
    private static final String METRIC_PERCENTAGE_FAILED = "cucumber_percentage_failed";
    private static final String METRIC_PERCENTAGE_SKIPPED = "cucumber_percentage_skipped";

    private final TestRunResults testRunResults;

    CucumberMetricReader(String cucumberJsonFilename) throws IOException {
        String fileContent = new String(Files.readAllBytes(Paths.get(cucumberJsonFilename)));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<Feature> features = objectMapper.readValue(fileContent, new TypeReference<List<Feature>>() {
        });
        testRunResults = new TestRunResults(features);
    }

    Map<String, Number> getMetrics() {
        Map<String, Number> metrics = new HashMap<>();

        metrics.put(METRIC_RUNTIME, testRunResults.getDuration());
        metrics.put(METRIC_RUNTIME_API, testRunResults.getDurationApiTests());
        metrics.put(METRIC_RUNTIME_UI, testRunResults.getDurationUiTests());

        long noOfTests = testRunResults.getNumberOfTests();
        metrics.put(METRIC_COUNT_TESTS, noOfTests);
        long noOfApiTests = testRunResults.getNumberOfApiTests();
        metrics.put(METRIC_COUNT_TESTS_API, noOfApiTests);
        long noOfUiTests = testRunResults.getNumberOfUiTests();
        metrics.put(METRIC_COUNT_TESTS_UI, noOfUiTests);

        long noOfSuccessfulTests = testRunResults.getNumberOfSuccessfulTests();
        metrics.put(METRIC_COUNT_TESTS_SUCCESSFUL, noOfSuccessfulTests);
        long noOfFailedTests = testRunResults.getNumberOfFailedTests();
        metrics.put(METRIC_COUNT_TESTS_FAILED, noOfFailedTests);
        long noOfSkippedTests = testRunResults.getNumberOfSkippedTests();
        metrics.put(METRIC_COUNT_TESTS_SKIPPED, noOfSkippedTests);

        if (noOfTests > 0) {
            metrics.put(METRIC_PERCENTAGE_SUCCESSFUL, (noOfSuccessfulTests + noOfSkippedTests) * PERCENT_FACTOR / noOfTests);
            metrics.put(METRIC_PERCENTAGE_FAILED, noOfFailedTests * PERCENT_FACTOR / noOfTests);
            metrics.put(METRIC_PERCENTAGE_SKIPPED, noOfSkippedTests * PERCENT_FACTOR / noOfTests);

            metrics.put(METRIC_PERCENTAGE_API, noOfApiTests * PERCENT_FACTOR / noOfTests);
            metrics.put(METRIC_PERCENTAGE_UI, noOfUiTests * PERCENT_FACTOR / noOfTests);
        }

        return metrics;
    }


}
