package org.dbsearch.services;

import com.google.gson.Gson;
import org.dbsearch.gsonPOJO.ErrorPOJO;

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
        }
    }

    //создание JSON-файла с сообщением об ошибке
    public void error(File outputFile, String errorMessage) {
        ErrorPOJO error = new ErrorPOJO(errorMessage);
        writeOutput(outputFile, g.toJson(error));
    }
}
