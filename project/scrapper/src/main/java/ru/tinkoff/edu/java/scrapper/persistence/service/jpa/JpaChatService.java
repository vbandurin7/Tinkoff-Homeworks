package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;

    @Override
    public ChatDto register(ChatSaveRequest chr) {
        chatRepository.save(chr.getEntityChat());
        return chr.getDtoChat();
    }

    @Override
    public void unregister(Long tgChatId) {
        if (count(tgChatId) != 0) {
            chatRepository.deleteById(tgChatId);
        }
    }

    @Override
    public long count(Long tgChatId) {
        return chatRepository.countById(tgChatId);
    }
}
