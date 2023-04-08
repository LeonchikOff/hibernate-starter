package org.example;

import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class HibernateRunner {


    public static void main(String[] args) throws SQLException {
        try (Connection connection = ConnectionManager.openConnection()) {
            System.out.println("new Connection");
        }
    }
}
