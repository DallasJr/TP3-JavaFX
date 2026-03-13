package com.example.tp3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:tickets.db";

    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite driver not found", e);
        }
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS support_tickets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "customer_name TEXT NOT NULL, " +
                "priority TEXT NOT NULL, " +
                "created_at TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "urgent INTEGER NOT NULL, " +
                "status TEXT NOT NULL" +
                ")"
            );
        }
    }
}
