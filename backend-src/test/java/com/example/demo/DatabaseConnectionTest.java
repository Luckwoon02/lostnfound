package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mysql?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "omnamasiva";

        System.out.println("Attempting to connect to MySQL...");
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Successfully connected to MySQL server!");
            System.out.println("MySQL Version: " + conn.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver Name: " + conn.getMetaData().getDriverName());
            System.out.println("Driver Version: " + conn.getMetaData().getDriverVersion());
        } catch (SQLException e) {
            System.err.println("Failed to connect to MySQL server!");
            e.printStackTrace();
        }
    }
}
