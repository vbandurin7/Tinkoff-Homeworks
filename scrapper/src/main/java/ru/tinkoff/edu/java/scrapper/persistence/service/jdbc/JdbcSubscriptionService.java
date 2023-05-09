package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractSubscriptionService;


public class JdbcSubscriptionService extends AbstractSubscriptionService {

    public JdbcSubscriptionService(JdbcLinkService linkService, JdbcChatService chatService, JdbcSubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.linkService = linkService;
        this.chatService = chatService;
    }
}
