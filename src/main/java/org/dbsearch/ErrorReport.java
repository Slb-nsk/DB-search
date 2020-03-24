package org.dbsearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ErrorReport {

    public void errorReporting(File file, String errorMessage) {
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write("{\n");
            writer.write("  \"type\":\"error\"\n");
            writer.write("  \"message\":\"");
            writer.write(errorMessage);
            writer.write("\"\n");
            writer.write("}\n");
            writer.close();

        } catch (IOException e) {
            System.out.println("Cannot write file");
            System.exit(1);
        }

    }
}
