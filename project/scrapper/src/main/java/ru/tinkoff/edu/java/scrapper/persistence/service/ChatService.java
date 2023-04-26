package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Chat;

public interface ChatService {
    Chat register(ChatSaveRequest chat);
    void unregister(Long tgChatId);
    long count(Long tgChatId);
}
