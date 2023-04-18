package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;

public interface ChatService {
    Chat register(Chat chat);
    void unregister(Long tgChatId);
    long count(Long tgChatId);
}
