package com.campusbazaar.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages JDBC connections to MySQL database.
 * Demonstrates Singleton Pattern for Database Connection Management.
 */
public class DBConnectionManager {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/campus_bazaar_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "root";

    private static DBConnectionManager instance;
    private String url;
    private String user;
    private String password;

    private DBConnectionManager() {
        this.url = System.getProperty("db.url", DEFAULT_URL);
        this.user = System.getProperty("db.user", DEFAULT_USER);
        this.password = System.getProperty("db.password", DEFAULT_PASS);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Driver may be provided in classpath or bundled jar
        }
    }

    public static synchronized DBConnectionManager getInstance() {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}

