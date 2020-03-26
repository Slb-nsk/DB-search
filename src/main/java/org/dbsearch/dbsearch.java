package org.dbsearch;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.dbsearch.services.OutputService;
import org.dbsearch.services.QueryService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Dbsearch {
    static JsonParser parser;

    public static void main(String[] args) {

        //Проверяем, все ли аргументы есть в командной строке
        if (args.length < 3) {
            System.out.println("Arguments list is incomplete");
            System.exit(1);
        }

        //берём аргументы из командной строки и присваиваем им читабельные имена
        String command = args[0];
        File inputFile = new File(args[1]);
        File outputFile = new File(args[2]);

        //проверяем, является ли первый аргумент легальной командой
        if (!(command.equals("search")) && !(command.equals("stat"))) {
            System.out.println("Unknown command");
            System.exit(1);
        } else {
            //считываем входной файл в строку
            StringBuilder contentBuilder = new StringBuilder();
            String jsonString;
            try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    contentBuilder.append(sCurrentLine).append("\n");
                }
                jsonString = contentBuilder.toString();

                //перерабатываем строку в JSON-объекты и передаём на обработку
                try {
                    JsonObject inputObject = parser.parseString(jsonString).getAsJsonObject();
                    QueryService service = new QueryService();
                    switch (command) {
                        case "search":
                            service.searchQuery(inputObject, outputFile);
                            break;
                        case "stat":
                            service.statQuery(inputObject, outputFile);
                            break;
                    }

                } catch (JsonParseException e) {
                    OutputService output = new OutputService();
                    output.error(outputFile, "Input file is not a valid JSON file.");
                }

                //если входной файл прочитать не удалось
            } catch (IOException e) {
                System.out.println("Cannot read file");
                e.printStackTrace();
                System.exit(1);
            }
        }

    }
}
