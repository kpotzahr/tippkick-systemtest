package com.capgemini.csd.tools;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CucumberMetricPoster {
    private static final int FAILURE = 1;
    private static final int EXPECTED_NO_OF_PARAMS = 4;
    private static final int PARAM_JSON_FILE = 0;
    private static final int PARAM_DB_URL = 1;
    private static final int PARAM_DB_NAME = 2;
    private static final int PARAM_APP_NAME = 3;

    private CucumberMetricReader generator;

    public CucumberMetricPoster(String inputJsonFile) throws IOException {
        try {
            generator = new CucumberMetricReader(inputJsonFile);
        } catch (FileNotFoundException exc) {
            log.warn("File not found - no results written", exc);
        }
    }

    public static void main(String[] args) {
        if (args.length < EXPECTED_NO_OF_PARAMS) {
            log.warn("Not enough arguments. No metrics posted to InfluxDB.\nUsage: CucumberInfluxMetricPoster inputJsonFile influxDbUrl influxDbName environment appName teamName");
            System.exit(FAILURE);
        }

        String inputJsonFile = args[PARAM_JSON_FILE];
        String influxDbUrl = args[PARAM_DB_URL];
        String influxDbName = args[PARAM_DB_NAME];
        String appName = args[PARAM_APP_NAME];
        try {
            new CucumberMetricPoster(inputJsonFile).writeTestdataToInfluxDb(influxDbUrl, influxDbName, appName);
        } catch (Exception exc) {
            log.error("error found", exc);
            System.exit(FAILURE);
        }
    }

    public void writeTestdataToInfluxDb(String influxDbUrl, String influxDbName, String appName) {
        if (null == generator) {
            return;
        }

        InfluxDB influxDB = InfluxDBFactory.connect(influxDbUrl, "root", "root");
        try {
            influxDB.createDatabase(influxDbName);

            BatchPoints batchPoints = BatchPoints
                    .database(influxDbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();

            for (Map.Entry<String, Number> metric : generator.getMetrics().entrySet()) {
                Point point = Point.measurement(metric.getKey().replace("_run_", "_percentage_"))
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .tag("appName", appName)
                        .addField("value", metric.getValue())
                        .build();

                batchPoints.point(point);
            }
            influxDB.write(batchPoints);
        } catch (Exception exc) {
            log.error("Could not write to influx db " + influxDbUrl
                    + " " + influxDbName + ". No metrics were written.", exc);
        }
    }
}


