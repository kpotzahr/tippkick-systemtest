package com.capgemini.csd.tippkick.cukes.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Component
class DbAccess {
    private static final String[] SPIELPLAN_TABLES = {"MATCH"};
    private static final String[] TIPPABGABE_TABLES = {"GAME_BET", "CLOSED_MATCH"};
    private static final String[] TIPPWERTUNG_TABLES = {"GAME_BET", "USER_SCORE"};

    @Value("${spielplan.db.url:jdbc:h2:tcp://localhost:7090/file:~/spielplan}")
    private String spielplanDbUrl;

    @Value("${spielplan.db.username:sa}")
    private String spielplanDbUser;

    @Value("${spielplan.db.password:}")
    private String spielplanDbPassword;

    @Value("${tippabgabe.db.url:jdbc:h2:tcp://localhost:7091/file:~/tippabgabe}")
    private String tippabgabeDbUrl;

    @Value("${tippabgabe.db.username:sa}")
    private String tippabgabeDbUser;

    @Value("${tippabgabe.db.password:}")
    private String tippabgabeDbPassword;

    @Value("${tippwertung.db.url:jdbc:h2:tcp://localhost:7092/file:~/tippwertung}")
    private String tippwertungDbUrl;

    @Value("${tippwertung.db.username:sa}")
    private String tippwertungDbUser;

    @Value("${tippwertung.db.password:}")
    private String tippwertungDbPassword;


    void cleanupData() throws SQLException {
        cleanupData(spielplanDbUrl, spielplanDbUser, spielplanDbPassword, SPIELPLAN_TABLES);
        cleanupData(tippabgabeDbUrl, tippabgabeDbUser, tippabgabeDbPassword, TIPPABGABE_TABLES);
        cleanupData(tippwertungDbUrl, tippwertungDbUser, tippwertungDbPassword, TIPPWERTUNG_TABLES);
    }

    private void cleanupData(String dbUrl, String dbUser, String dbPassword, String[] tables) throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", dbUser);
        connectionProps.put("password", dbPassword);

        try (Connection connection = DriverManager.getConnection(dbUrl, connectionProps);
             Statement stmt = connection.createStatement()) {
            for (String table : tables) {
                stmt.executeUpdate("delete from " + table);
            }
        }
    }

}
