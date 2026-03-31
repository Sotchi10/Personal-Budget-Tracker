package com.budgettracker.config;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    // Bro Soth's Account
    // private static final String URL = "jdbc:mysql://localhost:8889/budget_tracker";
    // private static final String USER = "root";
    // private static final String PASSWORD = "root";

    // Rith's Account
    private static final String URL = "jdbc:mysql://localhost:3306/budget_tracker_system";
    private static final String USER = "root";
    private static final String PASSWORD = "@RithzQ7";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("DB connection failed", e);
        }
    }

}
