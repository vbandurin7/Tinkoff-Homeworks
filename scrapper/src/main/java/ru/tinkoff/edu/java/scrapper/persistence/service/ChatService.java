package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;

public interface ChatService {

    ChatDto register(ChatSaveRequest chat);

    void unregister(Long tgChatId);

    long count(Long tgChatId);

    ChatDto findById(long tgChatId);
}
