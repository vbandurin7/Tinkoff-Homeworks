package ru.tinkoff.edu.java.scrapper.persistence.service.jooq;


import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractLinkService;
import ru.tinkoff.edu.java.scrapper.schedule.UpdateHandler;

@Service
public class JooqLinkService extends AbstractLinkService {
    public JooqLinkService(JooqLinkRepository linkRepository, StackoverflowClient stackoverflowClient, GitHubClient gitHubClient) {
        this.linkRepository = linkRepository;
        this.stackoverflowClient = stackoverflowClient;
        this.gitHubClient = gitHubClient;
    }
}
