package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.LinkInfoUpdater;
import ru.tinkoff.edu.java.scrapper.schedule.UpdateHandler;


public class JdbcLinkService extends AbstractLinkService {
    public JdbcLinkService(JdbcLinkRepository linkRepository, LinkInfoUpdater linkInfoUpdater) {
        this.linkRepository = linkRepository;
        this.linkInfoUpdater = linkInfoUpdater;
    }
}
