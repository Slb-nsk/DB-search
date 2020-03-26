package org.dbsearch.gsonPOJO;

import com.google.gson.JsonArray;

public class SearchPOJO {
    private String type = "search";
    private JsonArray results;

    public SearchPOJO(JsonArray results) {
        this.results = results;
    }
}
