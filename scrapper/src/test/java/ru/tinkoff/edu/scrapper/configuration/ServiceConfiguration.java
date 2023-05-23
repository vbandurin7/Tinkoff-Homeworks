package ru.tinkoff.edu.scrapper.configuration;

import org.jooq.DSLContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcSubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jooq.JooqChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jooq.JooqSubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaSubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.LinkInfoUpdater;

@TestConfiguration
@Profile("test")
public class ServiceConfiguration {

    @Bean
    @Primary
    public JpaLinkService jpaLinkService(JpaLinkRepository jpaLinkRepository, long checkInterval) {
        return new JpaLinkService(jpaLinkRepository, checkInterval);
    }

    @Bean
    @Primary
    public JpaChatService jpaChatService(JpaChatRepository jpaChatRepository) {
        return new JpaChatService(jpaChatRepository);
    }

    @Bean
    @Primary
    public JpaSubscriptionService jpaSubscriptionService(
        JpaLinkRepository linkRepository, JpaChatRepository jpaChatRepository, LinkInfoUpdater linkInfoUpdater) {
        return new JpaSubscriptionService(linkRepository, jpaChatRepository, linkInfoUpdater);
    }

    @Bean
    public JdbcLinkService jdbcLinkService(JdbcLinkRepository linkRepository, LinkInfoUpdater linkInfoUpdater) {
        return new JdbcLinkService(linkRepository, linkInfoUpdater);
    }

    @Bean
    public JdbcChatService jdbcChatService(JdbcChatRepository jdbcChatRepository) {
        return new JdbcChatService(jdbcChatRepository);
    }

    @Bean
    public JdbcSubscriptionService jdbcSubscriptionService(
        JdbcLinkRepository linkRepository,
        JdbcChatRepository jdbcChatRepository,
        JdbcSubscriptionRepository subscriptionRepository,
        LinkInfoUpdater linkInfoUpdater
    ) {
        return new JdbcSubscriptionService(
            new JdbcLinkService(linkRepository, linkInfoUpdater),
            new JdbcChatService(jdbcChatRepository),
            subscriptionRepository
        );
    }

    @Bean
    public JdbcChatRepository jdbcChatRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public JdbcLinkRepository jdbcLinkRepository(JdbcTemplate jdbcTemplate, long checkInterval) {
        return new JdbcLinkRepository(jdbcTemplate, checkInterval);
    }

    @Bean
    public JdbcSubscriptionRepository jdbcSubscriptionRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcSubscriptionRepository(jdbcTemplate);
    }

    @Bean
    public JooqLinkService jooqLinkService(JooqLinkRepository jooqLinkRepository, LinkInfoUpdater linkInfoUpdater) {
        return new JooqLinkService(jooqLinkRepository, linkInfoUpdater);
    }

    @Bean
    public JooqChatService jooqChatService(JooqChatRepository jooqChatRepository) {
        return new JooqChatService(jooqChatRepository);
    }

    @Bean
    public JooqSubscriptionService jooqSubscriptionService(
        JooqLinkRepository linkRepository, LinkInfoUpdater linkInfoUpdater,
        JooqChatRepository jooqChatRepository, JooqSubscriptionRepository subscriptionRepository
    ) {
        return new JooqSubscriptionService(new JooqLinkService(linkRepository, linkInfoUpdater),
            new JooqChatService(jooqChatRepository), subscriptionRepository
        );
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
