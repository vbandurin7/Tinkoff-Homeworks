package ru.tinkoff.edu.java.bot.bot.command;

public enum Commands {
    HELP("/help", "Show all commands"),
    START("/start", "Register new user"),
    TRACK("/track", "Start tracking new link by '/track <link>'"),
    UNTRACK("/untrack", "Stop tracking link by '/untrack <link>'"),
    LIST("/list", "Show all tracking links");


    private String command;
    private String description;

    Commands(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
