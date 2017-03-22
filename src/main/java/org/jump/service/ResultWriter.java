package org.jump.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {

    private static BufferedWriter writer = null;

    private static String fileName = null;

    public static void writeRow(String row) {
        if (writer == null) {
            try {
                writer = getWriter();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }

        try {
            writer.write(row);
            writer.newLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

    }

    public static void close() {
        if (writer != null) {
            try {
                writer.close();
                System.out.println("Output is stored in file: " + fileName);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static BufferedWriter getWriter() throws Exception {
        fileName = String.valueOf(System.currentTimeMillis()) + "_result.txt";
        FileWriter fileWriter = new FileWriter(fileName, true);
        return new BufferedWriter(fileWriter);
    }
}
