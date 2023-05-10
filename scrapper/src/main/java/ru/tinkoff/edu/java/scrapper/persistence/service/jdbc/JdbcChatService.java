package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractChatService;

public class JdbcChatService extends AbstractChatService {
    public JdbcChatService(JdbcChatRepository jdbcChatRepository) {
        this.chatRepository = jdbcChatRepository;
    }
}
