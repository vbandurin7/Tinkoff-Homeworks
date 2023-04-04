package ru.tinkoff.edu.java.bot.bot.command.commands;

public enum Command {
    HELP("/help", "Show all commands"),
    START("/start", "Register new user"),
    TRACK("/track", "Start tracking new link by '/track <link>'"),
    UNTRACK("/untrack", "Stop tracking link by '/untrack <link>'"),
    LIST("/list", "Show all tracking links");


    private String command;
    private String description;

    Command(String command, String description) {
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
