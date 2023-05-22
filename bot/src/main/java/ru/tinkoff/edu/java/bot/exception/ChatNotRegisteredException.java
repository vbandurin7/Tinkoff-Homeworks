package ru.tinkoff.edu.java.bot.exception;

import java.io.IOException;

public class ChatNotRegisteredException extends IOException {

    public ChatNotRegisteredException(String message) {
        super(message);
    }
}
