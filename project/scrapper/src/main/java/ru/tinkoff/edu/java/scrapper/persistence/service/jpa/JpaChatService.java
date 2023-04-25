package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;

@Service
@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;

    @Override
    public Chat register(Chat chat) {
        chatRepository.save(dtoToEntityChat(chat));
        return chat;
    }

    @Override
    public void unregister(Long tgChatId) {
        chatRepository.deleteById(tgChatId);
    }

    @Override
    public long count(Long tgChatId) {
        return chatRepository.countById(tgChatId);
    }

    private ru.tinkoff.edu.java.scrapper.persistence.entity.Chat dtoToEntityChat(Chat chat) {
        return new ru.tinkoff.edu.java.scrapper.persistence.entity.Chat(chat.getId());
    }
}
