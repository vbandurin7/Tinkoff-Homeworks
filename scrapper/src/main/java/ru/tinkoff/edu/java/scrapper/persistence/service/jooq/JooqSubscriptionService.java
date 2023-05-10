package ru.tinkoff.edu.java.scrapper.persistence.service.jooq;

import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractSubscriptionService;

public class JooqSubscriptionService extends AbstractSubscriptionService {

    public JooqSubscriptionService(
        JooqLinkService linkService, JooqChatService chatService,
        JooqSubscriptionRepository subscriptionRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.linkService = linkService;
        this.chatService = chatService;
    }
}
