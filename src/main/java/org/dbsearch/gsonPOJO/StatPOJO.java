package org.dbsearch.gsonPOJO;

import com.google.gson.JsonArray;

public class StatPOJO {
    private String type = "stat";
    private int totalDays;
    private JsonArray customers;
    private int totalExpenses;
    private double avgExpenses;

    public StatPOJO(int totalDays, JsonArray customers, int totalExpenses, double avgExpenses) {
        this.totalDays = totalDays;
        this.customers = customers;
        this.totalExpenses = totalExpenses;
        this.avgExpenses = avgExpenses;
    }
}
