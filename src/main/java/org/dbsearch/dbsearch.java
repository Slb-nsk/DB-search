package org.dbsearch;

import org.dbsearch.services.OutputService;
import org.dbsearch.services.QueryService;

import java.io.File;


public class Dbsearch {

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

        if (!(command.equals("search") || command.equals("stat"))) {
            OutputService output = new OutputService();
            output.error(outputFile, "Unknown command");
        } else {
            QueryService service = new QueryService();
            switch (command) {
                case "search":
                    service.searchQuery(inputFile, outputFile);
                    break;
                case "stat":
                    service.statQuery(inputFile, outputFile);
                    break;
            }
        }

    }
}
