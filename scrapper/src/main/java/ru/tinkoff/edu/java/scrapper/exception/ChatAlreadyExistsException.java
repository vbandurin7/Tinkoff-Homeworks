package ru.tinkoff.edu.java.scrapper.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatAlreadyExistsException extends RuntimeException {
    
    private final long chatId;
    private final String message;

}
