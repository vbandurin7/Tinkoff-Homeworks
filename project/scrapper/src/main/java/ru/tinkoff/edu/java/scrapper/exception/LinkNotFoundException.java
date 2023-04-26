package ru.tinkoff.edu.java.scrapper.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LinkNotFoundException extends RuntimeException {
    public LinkNotFoundException(String message) {
        super(message);
    }
}
