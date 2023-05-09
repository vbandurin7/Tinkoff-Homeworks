package ru.tinkoff.edu.java.bot.bot.command.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public abstract sealed class AbstractCommandProcessor implements CommandProcessor permits HelpProcessor, ListProcessor, StartProcessor, TrackProcessor, UntrackProcessor {

    @Override
    public String toString() {
        return command() + " - " + description();
    }

    protected SendMessage send(Update update, String message) {
        return new SendMessage(update.message().chat().id(), message).parseMode(ParseMode.HTML);
    }

}
