package org.jump.util;

import java.util.Scanner;

import org.jump.entity.ApplicationConfiguration;

public class Utility {

    public static boolean isNumeric(String string) {
        return string.matches("[-+]?\\d*\\.?\\d+");
    }

    public static String readFromConsole(String message) {
        System.out.print(message + ": ");
        Scanner in = new Scanner(System.in);

        return in.next();
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
}
