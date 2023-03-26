package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;

public class ClientConfiguration {

    @Bean(name = "GitHubClient")
    public GitHubClient gitHubClient(String url) {
        return new GitHubClient(url);
    }

    @Bean(name = "GitHubClient")
    public GitHubClient gitHubClient() {
        return new GitHubClient();
    }

    @Bean(name = "GitHubClient")
    public StackoverflowClient stackoverflowClient(String url) {
        return new StackoverflowClient(url);
    }

    @Bean(name = "GitHubClient")
    public StackoverflowClient stackoverflowClient() {
        return new StackoverflowClient();
    }

    @Bean
    public long schedulerInterval(ApplicationConfig config) {
        return config.scheduler().interval().toMillis();
    }
}
