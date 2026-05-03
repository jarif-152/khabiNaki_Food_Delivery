package com.fooddelivery.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {
    // for url give your own database name as url...mine was food_delivery
    private static final String URL = "jdbc:postgresql://localhost:5432/food_delivery";
    // for username and password also the one where the schema is that should be
    // used
    private static final String USER = "luimas834";
    private static final String PASSWORD = "luimas834";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}