package org.jump.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.jump.entity.ApplicationConfiguration;
import org.jump.entity.ExecutionStatus;

public class Utility {

    public static boolean isNumeric(String string) {
        return string.matches("[-+]?\\d*\\.?\\d+");
    }

    public static String readFromConsole(String message) {
        System.out.print(message + ": ");
        Scanner in = new Scanner(System.in);

        String userInput = in.next();
        in.close();

        return userInput;
    }

    public static String buildUrl(ApplicationConfiguration appConfig) {

        String port = appConfig.getPort() == null ? "3306" : String.valueOf(appConfig.getPort());
        String host = appConfig.getHost() == null ? "localhost" : appConfig.getHost();

        String url = String.format("jdbc:mysql://%s:%s/%s", host, port, appConfig.getDatabase());

        return url;
    }

    public static String wrapAndEscape(String item) {
        return wrap(escape(item));
    }

    public static String wrap(String item) {
        if (item == null) {
            return "NULL";
        }

        return "'" + item + "'";
    }

    public static String escape(String item) {
        return item.replace("'", "\\'");
    }

    public static String getFileContents(String fileName) throws Exception {
        byte[] content = Files.readAllBytes(Paths.get(fileName));
        return new String(content);
    }

    public static String getExecutionStatus(ExecutionStatus executionStatus) {
        switch (executionStatus) {
            case FAILED:
                return "Failed with error.";

            case MANUAL_ROLLBACK:
                return "Executed and rolled back successfully.";

            case SUCCESSFUL:
                return "Success!";

            case SUCCESSFUL_DRY_RUN:
                return "Dry run completed successfully.";

            case PARSE_FAILURE:
                return "There is an error in input file.";

            default:
                throw new RuntimeException("Execution status not defined: " + executionStatus.toString());
        }
    }

    public static Double getDoubleValue(String item) {
        if (!Utility.isNumeric(item)) {
            String message = String.format("%s couldn't be cast to integer.", item);
            throw new RuntimeException(message);
        }

        return Double.parseDouble(item);
    }
}
