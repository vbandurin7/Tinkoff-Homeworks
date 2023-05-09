package ru.tinkoff.edu.java.scrapper.configuration;

import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.SubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jooq.JooqChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jooq.JooqSubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.LinkInfoUpdater;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public LinkService linkService(JooqLinkRepository linkRepository, LinkInfoUpdater linkInfoUpdater) {
        return new JooqLinkService(linkRepository, linkInfoUpdater);
    }
    @Bean
    public ChatService chatService(JooqChatRepository jooqChatRepository) {
        return new JooqChatService(jooqChatRepository);
    }
    @Bean
    public SubscriptionService subscriptionService(JooqLinkRepository linkRepository, LinkInfoUpdater linkInfoUpdater, JooqChatRepository jooqChatRepository, JooqSubscriptionRepository subscriptionRepository) {
        return new JooqSubscriptionService(new JooqLinkService(linkRepository, linkInfoUpdater), new JooqChatService(jooqChatRepository), subscriptionRepository);
    }

    @Bean
    public JooqChatRepository jooqChatRepository(DSLContext dslContext) {
        return new JooqChatRepository(dslContext);
    }

    @Bean
    public JooqLinkRepository jooqLinkRepository(DSLContext dslContext, long checkInterval) {
        return new JooqLinkRepository(dslContext, checkInterval);
    }

    @Bean
    public JooqSubscriptionRepository jooqSubscriptionRepository(DSLContext dslContext) {
        return new JooqSubscriptionRepository(dslContext);
    }

}