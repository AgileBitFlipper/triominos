package com.thirdsonsoftware;

public class Log {

    public static boolean debugMode = false ;

    // Foreground colors
    public static final String RESET  = "\u001B[0m";
    public static final String BLACK  = "\u001B[30m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN   = "\u001B[36m";
    public static final String WHITE  = "\u001B[37m";

    // Background colors
    public static final String BLACK_BG  = "\u001B[40m";
    public static final String RED_BG    = "\u001B[41m";
    public static final String GREEN_BG  = "\u001B[42m";
    public static final String YELLOW_BG = "\u001B[43m";
    public static final String BLUE_BG   = "\u001B[44m";
    public static final String PURPLE_BG = "\u001B[45m";
    public static final String CYAN_BG   = "\u001B[46m";
    public static final String WHITE_BG  = "\u001B[47m";

    // Retrieve the calling class
    public static Class getCallerClass(int level) throws ClassNotFoundException {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String rawFQN = stElements[level+1].toString().split("\\(")[0];
        return Class.forName(rawFQN.substring(0, rawFQN.lastIndexOf('.')));
    }

    //info
    public static void Info(String message) {
        System.out.println(WHITE + message + RESET);
    }

    //error
    public static void Error(String message) {
        System.out.println(RED + message + RESET);
    }

    //debug
    public static void Debug(String message) {
        if ( debugMode )
            System.out.println(BLUE + message + RESET);
    }

    //warning
    public static void Warning(String message) {
        System.out.println(YELLOW + message + RESET);
    }

    // Todo: Add logging to a file support
}
