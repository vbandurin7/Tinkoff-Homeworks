package ru.tinkoff.edu.java.scrapper.persistence.service.jooq;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractChatService;

@Service
public class JooqChatService extends AbstractChatService {
    public JooqChatService(JooqChatRepository jooqChatRepository) {
        this.chatRepository = jooqChatRepository;
    }
}