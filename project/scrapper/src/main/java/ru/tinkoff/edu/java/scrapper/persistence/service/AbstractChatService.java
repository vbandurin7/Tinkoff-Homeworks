package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.ChatRepository;

public abstract class AbstractChatService implements ChatService {

    protected ChatRepository chatRepository;

    @Override
    public Chat register(ChatSaveRequest chr) {
        long tgChatId = chr.getDtoChat().getId();
        validateId(tgChatId);
        if (count(tgChatId) != 0) {
            return chr.getDtoChat();
        }
        return chatRepository.save(chr.getDtoChat());
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
