package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.SubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaSubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.LinkInfoUpdater;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public LinkService linkService(JpaLinkRepository linkRepository, long checkInterval) {
        return new JpaLinkService(linkRepository, checkInterval);
    }
    @Bean
    public ChatService chatService(JpaChatRepository jpaChatRepository) {
        return new JpaChatService(jpaChatRepository);
    }
    @Bean
    public SubscriptionService subscriptionService(JpaLinkRepository linkRepository, JpaChatRepository jpaChatRepository, LinkInfoUpdater linkInfoUpdater) {
        return new JpaSubscriptionService(linkRepository, jpaChatRepository, linkInfoUpdater);
    }
}