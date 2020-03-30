package org.dbsearch.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.dbsearch.services.OutputService;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DbDAO {

    private static final String SELECT_CUSTOMERS_WITH_SELECTED_LAST_NAME
            = "SELECT firstName FROM customers WHERE lastName=?";
    private static final String CUSTOMERS_BOUGHT_THIS_PRODUCT_AT_LEAST_THIS_TIMES
            = "SELECT lastName, firstName\n" +
            "FROM customers\n" +
            "WHERE customerId IN\n" +
            "(SELECT customerId \n" +
            "FROM purchases JOIN products\n" +
            "ON purchases.productId = products.productId\n" +
            "AND products.productName = ?\n" +
            "GROUP BY customerId\n" +
            "HAVING COUNT(customerId) > ?)";
    private static final String CUSTOMERS_SPENT_MONEY_BETWEEN_THIS_AND_THIS
            = "SELECT lastName, firstName\n" +
            "FROM customers\n" +
            "WHERE customerId IN\n" +
            "(SELECT customerId \n" +
            "FROM purchases JOIN products\n" +
            "ON purchases.productId = products.productId\n" +
            "GROUP BY customerId\n" +
            "HAVING SUM(price) BETWEEN ? AND ?);";
    private static final String CUSTOMERS_ORDERED_BY_NUMBER_OF_PURCHASING
            = "SELECT customers.lastName, customers.firstName\n" +
            "FROM customers JOIN purchases\n" +
            "ON customers.customerId = purchases.customerId\n" +
            "GROUP BY customers.customerId\n" +
            "ORDER BY COUNT(purchases.customerId) ASC";
    private static final String STATISTIC_FROM_PERIOD
            = "SELECT customers.customerId, customers.lastName, customers.firstName, products.productName, SUM(products.price) as expenses\n" +
            "FROM customers JOIN purchases\n" +
            " ON customers.customerId = purchases.customerId\n" +
            " JOIN products\n" +
            " ON purchases.productId = products.productId \n" +
            "WHERE purchases.purchaseId IN\n" +
            "(SELECT purchaseId\n" +
            "FROM purchases\n" +
            "WHERE purchases.purchaseDate BETWEEN ? AND ?)\n" +
            "GROUP BY customers.customerId, products.productName\n" +
            "ORDER BY expenses DESC;";

    private final OutputService output = new OutputService();

    private ConnectionBuilder builder = new SimpleConnectionBuilder();

    private Connection getConnection() throws SQLException {
        return builder.getConnection();
    }

    public JsonArray customersWithSelectedLastName(File outputFile, String lastName) {
        JsonArray list = new JsonArray();
        try (Connection con = getConnection()) {
            PreparedStatement pst = con.prepareStatement(SELECT_CUSTOMERS_WITH_SELECTED_LAST_NAME);
            pst.setString(1, lastName);
            ResultSet rs = pst.executeQuery();
            list = formListOfCustomersWithThisLastName(rs, lastName);
            rs.close();
            pst.close();
        } catch (SQLException e) {
            output.error(outputFile, "No connection with database");
        }
        return list;
    }

    public JsonArray customersBoughtThisProductAtLeastThisTimes(File outputFile, String productName, int minTimes) {
        JsonArray list = new JsonArray();
        try (Connection con = getConnection()) {
            PreparedStatement pst = con.prepareStatement(CUSTOMERS_BOUGHT_THIS_PRODUCT_AT_LEAST_THIS_TIMES);
            pst.setString(1, productName);
            pst.setInt(2, minTimes);
            ResultSet rs = pst.executeQuery();
            list = formListOfCustomers(rs);
            rs.close();
            pst.close();
        } catch (SQLException e) {
            output.error(outputFile, "No connection with database");
        }
        return list;
    }

    public JsonArray customersSpentMoneyBetweenThisAndThis(File outputFile, int minExpenses, int maxExpenses) {
        JsonArray list = new JsonArray();
        try (Connection con = getConnection()) {
            PreparedStatement pst = con.prepareStatement(CUSTOMERS_SPENT_MONEY_BETWEEN_THIS_AND_THIS);
            pst.setInt(1, minExpenses);
            pst.setInt(2, maxExpenses);
            ResultSet rs = pst.executeQuery();
            list = formListOfCustomers(rs);
            rs.close();
            pst.close();
        } catch (SQLException e) {
            output.error(outputFile, "No connection with database");
        }
        return list;
    }

    public JsonArray badCustomers(File outputFile, int badCustomersNumber) {
        JsonArray list = new JsonArray();
        try (Connection con = getConnection()) {
            PreparedStatement pst = con.prepareStatement(CUSTOMERS_ORDERED_BY_NUMBER_OF_PURCHASING);
            ResultSet rs = pst.executeQuery();
            list = formListOfCustomers(rs);
            rs.close();
            pst.close();
            while (list.size() > badCustomersNumber) {
                list.remove(list.size() - 1);
            }
        } catch (SQLException e) {
            output.error(outputFile, "No connection with database");
        }
        return list;
    }

    public JsonArray statisticFromPeriod(File outputFile, LocalDate startDate, LocalDate endDate) {
        JsonArray list = new JsonArray();
        try (Connection con = getConnection()) {
            PreparedStatement pst = con.prepareStatement(STATISTIC_FROM_PERIOD);
            pst.setObject(1, startDate);
            pst.setObject(2, endDate);
            ResultSet rs = pst.executeQuery();
            int customerId = 0;
            JsonObject customer = new JsonObject();
            JsonArray purchases = new JsonArray();
            int totalExpenses = 0;
            while (rs.next()) {
                if (customerId != rs.getInt("customerId")) {
                    if (customerId != 0) {
                        customer.add("purchases", purchases);
                        customer.add("totalExpenses", new JsonPrimitive(totalExpenses));
                        list.add(customer);
                    }
                    customerId = rs.getInt("customerId");
                    customer = new JsonObject();
                    String name = rs.getString("lastName") + " " + rs.getString("firstName");
                    customer.add("name", new JsonPrimitive(name));
                    purchases = new JsonArray();
                    totalExpenses = 0;
                }
                JsonObject purchase = new JsonObject();
                purchase.add("name", new JsonPrimitive(rs.getString("productName")));
                purchase.add("expenses", new JsonPrimitive(rs.getInt("expenses")));
                totalExpenses += rs.getInt("expenses");
                purchases.add(purchase);
            }
            if (customerId != 0) {
                customer.add("purchases", purchases);
                customer.add("totalExpenses", new JsonPrimitive(totalExpenses));
                list.add(customer);
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            output.error(outputFile, "No connection with database");
        }
        return list;
    }

    private JsonArray formListOfCustomersWithThisLastName(ResultSet rs, String lastName) throws SQLException {
        JsonArray list = new JsonArray();
        while (rs.next()) {
            JsonObject customer = new JsonObject();
            customer.add("lastName", new JsonPrimitive(lastName));
            customer.add("firstName", new JsonPrimitive(rs.getString("firstName")));
            list.add(customer);
        }
        return list;
    }

    private JsonArray formListOfCustomers(ResultSet rs) throws SQLException {
        JsonArray list = new JsonArray();
        while (rs.next()) {
            JsonObject customer = new JsonObject();
            customer.add("lastName", new JsonPrimitive(rs.getString("lastName")));
            customer.add("firstName", new JsonPrimitive(rs.getString("firstName")));
            list.add(customer);
        }
        return list;
    }

}
