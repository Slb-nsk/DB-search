package org.dbsearch.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.dbsearch.gsonPOJO.ErrorPOJO;
import org.dbsearch.gsonPOJO.SearchPOJO;
import org.dbsearch.gsonPOJO.StatPOJO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputService {
    Gson g = new Gson();

    //единое место создания выходного файла
    private void writeOutput(File outputFile, String jsonString) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(jsonString);
            writer.close();
        } catch (IOException e) {
            System.out.println("Cannot write file");
            e.printStackTrace();
            System.exit(1);
        }
    }

    //создание JSON-файла с сообщением об ошибке
    public void error(File outputFile, String errorMessage) {
        ErrorPOJO error = new ErrorPOJO(errorMessage);
        writeOutput(outputFile, g.toJson(error));
        System.exit(0);
    }

    //создание JSON-файла со списками покупателей по заданным критериям
    public void search(File outputFile, JsonArray results){
        SearchPOJO searchResult = new SearchPOJO(results);
        writeOutput(outputFile, g.toJson(searchResult));
    }

    //создание JSON-файла со статистикой покупок за период
    public void stat(File outputFile, int totalDays, JsonArray customers, int totalExpenses, double avgExpenses){
        StatPOJO results = new StatPOJO(totalDays, customers, totalExpenses, avgExpenses);
        writeOutput(outputFile, g.toJson(results));
    }
}
