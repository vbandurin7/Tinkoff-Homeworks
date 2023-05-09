package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.SubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcSubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.LinkInfoUpdater;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public LinkService linkService(JdbcLinkRepository linkRepository, LinkInfoUpdater linkInfoUpdater) {
        return new JdbcLinkService(linkRepository, linkInfoUpdater);
    }
    @Bean
    public ChatService chatService(JdbcChatRepository jdbcChatRepository) {
        return new JdbcChatService(jdbcChatRepository);
    }
    @Bean
    public SubscriptionService subscriptionService(JdbcLinkRepository linkRepository, JdbcChatRepository jdbcChatRepository, JdbcSubscriptionRepository subscriptionRepository, LinkInfoUpdater linkInfoUpdater) {
        return new JdbcSubscriptionService(new JdbcLinkService(linkRepository, linkInfoUpdater), new JdbcChatService(jdbcChatRepository), subscriptionRepository);
    }

    @Bean JdbcChatRepository jdbcChatRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public JdbcLinkRepository jdbcLinkRepository(JdbcTemplate jdbcTemplate, long checkInterval) {
        return new JdbcLinkRepository(jdbcTemplate, checkInterval);
    }

    @Bean
    public JdbcSubscriptionRepository subscriptionRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcSubscriptionRepository(jdbcTemplate);
    }
}