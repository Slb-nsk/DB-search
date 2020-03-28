package org.dbsearch.dao;

import org.dbsearch.services.OutputService;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbDAO {

    private static final String SELECT_CUSTOMERS_WITH_SELECTED_LAST_NAME
            = "SELECT firstName FROM customers WHERE lastName=?";

    private final OutputService output = new OutputService();

    private ConnectionBuilder builder = new SimpleConnectionBuilder();

    private Connection getConnection() throws SQLException {
        return builder.getConnection();
    }

    //SELECT firstName FROM customers WHERE lastName='lastName'
    public List<String> customersWithSelectedLastName(File outputFile, String lastName) {
        List<String> list = new ArrayList<>();
        try (Connection con = getConnection()) {
            PreparedStatement pst = con.prepareStatement(SELECT_CUSTOMERS_WITH_SELECTED_LAST_NAME);
            pst.setString(1, lastName);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("firstName"));
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            output.error(outputFile, "No connection with database");
        }
        return list;
    }

}
