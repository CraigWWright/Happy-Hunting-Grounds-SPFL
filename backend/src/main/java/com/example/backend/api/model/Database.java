package com.example.backend.api.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:spfl.sqlite";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

}
