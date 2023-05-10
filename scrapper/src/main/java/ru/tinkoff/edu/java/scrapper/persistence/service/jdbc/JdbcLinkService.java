package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.LinkInfoUpdater;

public class JdbcLinkService extends AbstractLinkService {

    public JdbcLinkService(JdbcLinkRepository linkRepository, LinkInfoUpdater linkInfoUpdater) {
        this.linkRepository = linkRepository;
        this.linkInfoUpdater = linkInfoUpdater;
    }
}
