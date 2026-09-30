package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {

    // Base URL pointing only to the XAMPP MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String dbName = "HeartBreak";
    private static final String playersTable = "players";
    private static final String usersTable = "users";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL + dbName, USER, PASSWORD);
    }

    public static void initializeDB() {
        String sql = "CREATE DATABASE IF NOT EXISTS " + dbName;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("Database creation is working");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initializeTables() {
        String dbUrl = URL + dbName;

        String USERS_SQL = "CREATE TABLE IF NOT EXISTS " + usersTable + "("
                + " uid INT AUTO_INCREMENT PRIMARY KEY,"
                + " username VARCHAR(50) NOT NULL UNIQUE,"
                + " pasword_hash varchar(255) NOT NULL"
                + " );";

        String PLAYERS_SQL = "CREATE TABLE IF NOT EXISTS " + playersTable + " ("
                + " id INT AUTO_INCREMENT PRIMARY KEY,"
                + " username VARCHAR(50) NOT NULL UNIQUE,"
                + " level INT DEFAULT 0,"
                + " FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE ON UPDATE CASCADE"
                + ");";

        try (Connection conn = DriverManager.getConnection(dbUrl, USER, PASSWORD);
                Statement stmt = conn.createStatement()) {

            // Initialize 'users' first cause 'players(username)' is an FK
            stmt.execute(USERS_SQL);
            stmt.execute(PLAYERS_SQL);
            System.out.println("Table creation works");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}