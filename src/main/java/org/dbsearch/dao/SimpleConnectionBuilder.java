package org.dbsearch.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class SimpleConnectionBuilder implements ConnectionBuilder {
    static final String DATA_FILE = "program.ini";
    static final String DRIVER = "org.postgresql.Driver";

    public SimpleConnectionBuilder() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {

        String url = "";
        String username = "";
        String password = "";

        //считываем параметры для соединения с базой данных
        try {
            BufferedReader br = new BufferedReader(new FileReader(DATA_FILE));
            ArrayList<String> parameters = new ArrayList<>();
            String s;
            while ((s = br.readLine()) != null) {
                parameters.add(s);
            }
            for (String stroka : parameters) {
                String[] words = stroka.split("=");
                switch (words[0].trim()) {
                    case "DB_URL":
                        url = "jdbc:postgresql://" + words[1].trim().substring(1,words[1].length()-2);
                        break;
                    case "USERNAME":
                        username = words[1].trim().substring(1,words[1].length()-2);
                        break;
                    case "PASSWORD":
                        password = words[1].trim().substring(1,words[1].length()-2);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return DriverManager.getConnection(url, username, password);
    }
}
