package ru.tinkoff.edu.java.bot.bot.command.processor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.bot.command.processor.AbstractCommandProcessor;

public sealed interface CommandProcessor permits AbstractCommandProcessor {

    String command();

    String description();
    SendMessage handle(Update update);



    default boolean supports(Update update) {
        return update.message().text().split(" ")[0].equals(command());
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }

}