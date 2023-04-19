package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.ChatRepository;

public abstract class AbstractChatService implements ChatService {

    protected ChatRepository chatRepository;

    @Override
    public Chat register(Chat chat) {
        long tgChatId = chat.getId();
        validateId(tgChatId);
        if (count(tgChatId) != 0) {
            return chat;
        }
        return chatRepository.save(chat);
    }

    @Override
    public void unregister(Long tgChatId) {
        validateId(tgChatId);
        chatRepository.deleteById(tgChatId);
    }

    @Override
    public long count(Long tgChatId) {
        return chatRepository.countById(tgChatId);
    }

    private void validateId(Long tgChatId) {
        if (tgChatId == null || tgChatId < 0 || !tgChatId.toString().matches("^[0-9]{1,10}$")) {
            throw new ChatNotFoundException();
        }
    }
}
