package org.example.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.example.util.JDBCPropertiesLoader.*;

@UtilityClass
public class ConnectionPoolManager {
    private static final String PROP_JDBC_DRIVER = "db.driver";
    private static final String PROP_URL = "db.url";
    private static final String PROP_USER = "db.username";
    private static final String PROP_PASSWORD = "db.password";
    private static final String PROP_POLL_SIZE_CUSTOM = "db.pool.size.custom";
    private static final String PROP_POLL_SIZE_DEFAULT = "db.pool.size.default";

    private static BlockingQueue<Connection> proxyConnectionPool;
    private static List<Connection> realConnections;


    static {
        loadDriver();
        initConnectionPool();
    }

    private static void loadDriver() {
        try {
            Class.forName(getProperty(PROP_JDBC_DRIVER));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initConnectionPool() {
        String propertyPoolSize = getProperty(PROP_POLL_SIZE_CUSTOM);
        int connectionsPoolSize =
                (propertyPoolSize == null)
                ? Integer.parseInt(getProperty(PROP_POLL_SIZE_DEFAULT))
                : Integer.parseInt(propertyPoolSize);
        proxyConnectionPool = new ArrayBlockingQueue<>(connectionsPoolSize);
        realConnections = new ArrayList<>(connectionsPoolSize);
        for (int i = 0; i < connectionsPoolSize; i++) {
            Connection connection = openConnection();
            realConnections.add(connection);
            Connection proxyConnection =
                    (Connection) Proxy.newProxyInstance(ConnectionPoolManager.class.getClassLoader(), new Class[]{Connection.class},
                            (proxy, method, args) -> method.getName().equals("close")
                                    ? proxyConnectionPool.add((Connection) proxy) : method.invoke(connection, args));
            proxyConnectionPool.add(proxyConnection);
        }
    }

    private static Connection openConnection() {
        try {
            return DriverManager.getConnection(
                    getProperty(PROP_URL),
                    getProperty(PROP_USER),
                    getProperty(PROP_PASSWORD)
            );
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static Connection getConnectionFromPool() {
        try {
            return proxyConnectionPool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close() {
        try {
            for (Connection connection : realConnections)
                connection.close();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
