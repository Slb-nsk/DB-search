package org.dbsearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Dbsearch {

    static void writeOutput(File file) {
//       try {
//            FileWriter writer = new FileWriter(file, false);
//            writer.flush();
//            writer.close();
//
//        } catch (IOException e) {
//            System.out.println("Cannot write file");
//            System.exit(1);
//        }

   }

    public static void main(String[] args) {
        ErrorReport er = new ErrorReport();

        //Проверяем, все ли аргументы есть в командной строке
        if (args.length < 3) {
            System.out.println("Arguments list is incomplete");
            System.exit(1);
        }

        String command = args[0];
        File inputFile = new File(args[1]);
        File outputFile = new File(args[2]);

        if (!(command.equals("search") || command.equals("stat"))) {
            er.errorReporting(outputFile, "Unknown command");
        } else {
            switch (command) {
                case "search":
                    //сделать поиск
                    break;
                case "stat":
                    //собрать статистику
                    break;
            }
        }
    }
}
