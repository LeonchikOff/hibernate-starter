package org.example.jdbc.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public class JDBCPropertiesLoader {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream resourceAsStream = JDBCPropertiesLoader.class.getClassLoader().getResourceAsStream("jdbc.properties")) {
            PROPERTIES.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String propertyName) {
        return PROPERTIES.getProperty(propertyName);
    }
}
