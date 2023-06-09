package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ClientConfiguration {

    @Bean(name = "scrapperClient")
    public ScrapperClient scrapperClient(ApplicationProperties applicationProperties) {
        return new ScrapperClient(applicationProperties.api().scrapperBaseUrl());
    }
}
