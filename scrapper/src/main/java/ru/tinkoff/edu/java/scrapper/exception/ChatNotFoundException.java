package ru.tinkoff.edu.java.scrapper.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(String message) {
        super(message);
    }
}