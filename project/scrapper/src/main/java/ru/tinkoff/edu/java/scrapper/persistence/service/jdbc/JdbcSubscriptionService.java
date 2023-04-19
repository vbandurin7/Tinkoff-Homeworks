package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractSubscriptionService;


@Service
@Primary
public class JdbcSubscriptionService extends AbstractSubscriptionService {

    public JdbcSubscriptionService(JdbcLinkService linkService, JdbcChatService chatService, JdbcSubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.jdbcLinkService = linkService;
        this.jdbcChatService = chatService;
    }
}
