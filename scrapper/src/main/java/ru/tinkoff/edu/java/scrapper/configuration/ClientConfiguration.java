package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;

@Configuration
public class ClientConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public BotClient botClient(ApplicationProperties applicationProperties) {
        return new BotClient(applicationProperties.botBaseUrl());
    }

    @Bean
    public StackoverflowClient stackoverflowClient(ApplicationProperties applicationProperties) {
        return new StackoverflowClient(applicationProperties.stackoverflowBaseUrl());
    }

    @Bean
    public GitHubClient gitHubClient(ApplicationProperties applicationProperties) {
        return new GitHubClient(applicationProperties.githubBaseUrl());
    }
}
