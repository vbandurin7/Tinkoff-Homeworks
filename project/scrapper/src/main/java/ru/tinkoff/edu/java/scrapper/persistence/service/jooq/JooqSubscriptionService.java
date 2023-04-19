package ru.tinkoff.edu.java.scrapper.persistence.service.jooq;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractSubscriptionService;

@Service
public class JooqSubscriptionService extends AbstractSubscriptionService {

    public JooqSubscriptionService(JooqLinkService linkService, JooqChatService chatService, JooqSubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.jdbcLinkService = linkService;
        this.jdbcChatService = chatService;
    }
}
