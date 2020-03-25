package org.dbsearch.gsonPOJO;

public class ErrorPOJO {
    private String type = "error";
    private String message;

    public ErrorPOJO(String message) {
        this.message = message;
    }
}
