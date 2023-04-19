package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractChatService;

@Service
@Primary
public class JdbcChatService extends AbstractChatService {
    public JdbcChatService(JdbcChatRepository jdbcChatRepository) {
        this.chatRepository = jdbcChatRepository;
    }
}
