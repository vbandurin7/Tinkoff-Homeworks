package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractLinkService;
import ru.tinkoff.edu.java.scrapper.schedule.UpdateHandler;


@Service
@Primary
public class JdbcLinkService extends AbstractLinkService {
    public JdbcLinkService(JdbcLinkRepository linkRepository, StackoverflowClient stackoverflowClient, GitHubClient gitHubClient) {
        this.linkRepository = linkRepository;
        this.stackoverflowClient = stackoverflowClient;
        this.gitHubClient = gitHubClient;
    }
}
