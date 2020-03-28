package org.dbsearch.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionBuilder implements ConnectionBuilder {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/base1";
    static final String USERNAME = "postgres";
    static final String PASSWORD = "";
    static final String DRIVER = "org.postgresql.Driver";

    public SimpleConnectionBuilder() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        String url = DB_URL;
        String username = USERNAME;
        String password = PASSWORD;
        return DriverManager.getConnection(url, username, password);
    }
}
