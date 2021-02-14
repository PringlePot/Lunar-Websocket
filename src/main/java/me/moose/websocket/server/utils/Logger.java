package me.moose.websocket.server.utils;

import lombok.Getter;

public class Logger {
    @Getter private String name;
    public Logger(String name) {
        this.name = name;
    }
    public void info(String input) {
        System.out.println("[INFO] | " + name + " | " + input);
    }

    public void warn(String input) {
        System.out.println(ConsoleColors.YELLOW_BRIGHT.getCode() + "[WARN] | " + name + " | " + input + ConsoleColors.RESET.getCode());
    }
    public void error(String input) {
        System.out.println(ConsoleColors.RED_BRIGHT.getCode() + "[ERROR] | " + name + " | " + input + ConsoleColors.RESET.getCode());
    }

    public void fatal(String input) {
        System.out.println(ConsoleColors.RED_BOLD_BRIGHT.getCode() + "[FATAL] | " + name + " | " + input + ConsoleColors.RESET.getCode());
    }
    public void sucess(String input) {
        System.out.println(ConsoleColors.GREEN_BRIGHT.getCode() + "[INFO] | " + name + " | " + input + ConsoleColors.RESET.getCode());
    }
}
