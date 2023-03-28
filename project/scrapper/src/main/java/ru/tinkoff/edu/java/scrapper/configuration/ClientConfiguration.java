package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;

@Configuration
public class ClientConfiguration {

    @Bean(name = "gitHubClient")
    public GitHubClient gitHubClient() {
        return new GitHubClient();
    }

    @Bean(name = "stackoverflowClient")
    public StackoverflowClient stackoverflowClient() {
        return new StackoverflowClient();
    }

    @Bean
    public long schedulerInterval(ApplicationConfig config) {
        return config.scheduler().interval().toMillis();
    }


}
