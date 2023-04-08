package org.example.util;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
public class ConnectionManager {
    private static final String PROP_JDBC_DRIVER = "db.driver";
    private static final String PROP_URL = "db.url";
    private static final String PROP_USER = "db.username";
    private static final String PROP_PASSWORD = "db.password";

    static {
        loadDriver();
    }

    private static void loadDriver() {
        try {
            Class.forName(JDBCPropertiesLoader.getProperty(PROP_JDBC_DRIVER));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection openConnection() {
        try {
            return DriverManager.getConnection(
                    JDBCPropertiesLoader.getProperty(PROP_URL),
                    JDBCPropertiesLoader.getProperty(PROP_USER),
                    JDBCPropertiesLoader.getProperty(PROP_PASSWORD)
            );
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
