package org.jump.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger {

    private static FileLogger instance = new FileLogger();

    public static FileLogger getInstance() {
        return instance;
    }

    private FileLogger() {

    }

    private BufferedWriter writer = null;

    private String fileName = null;

    public void writeRow(String row) {
        if (this.writer == null) {
            try {
                this.writer = getWriter();
            } catch (Exception e) {

                e.printStackTrace();
                return;
            }
        }

        try {
            this.writer.write(row);
            this.writer.newLine();
        } catch (IOException e) {

            e.printStackTrace();
            return;
        }

    }

    public void close() {
        if (this.writer != null) {
            try {
                this.writer.close();
                System.out.println("Output is stored in file: " + this.fileName);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    private BufferedWriter getWriter() throws Exception {
        this.fileName = String.valueOf(System.currentTimeMillis()) + "_result.txt";
        FileWriter fileWriter = new FileWriter(fileName, true);
        return new BufferedWriter(fileWriter);
    }
}
