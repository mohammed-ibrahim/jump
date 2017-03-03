package org.jump.util;

import java.util.Scanner;

public class Utility {

    public static boolean isNumeric(String string) {
        return string.matches("[-+]?\\d*\\.?\\d+");
    }

    public static String readFromConsole(String message) {
        System.out.print(message + ": ");
        Scanner in = new Scanner(System.in);

        return in.next();
    }
}
