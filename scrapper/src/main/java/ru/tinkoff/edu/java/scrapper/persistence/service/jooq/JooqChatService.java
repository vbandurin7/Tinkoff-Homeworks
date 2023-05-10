package ru.tinkoff.edu.java.scrapper.persistence.service.jooq;

import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractChatService;

public class JooqChatService extends AbstractChatService {
    public JooqChatService(JooqChatRepository jooqChatRepository) {
        this.chatRepository = jooqChatRepository;
    }
}
