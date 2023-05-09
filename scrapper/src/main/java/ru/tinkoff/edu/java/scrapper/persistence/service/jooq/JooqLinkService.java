package ru.tinkoff.edu.java.scrapper.persistence.service.jooq;


import ru.tinkoff.edu.java.scrapper.persistence.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.AbstractLinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.LinkInfoUpdater;

public class JooqLinkService extends AbstractLinkService {
    public JooqLinkService(JooqLinkRepository linkRepository, LinkInfoUpdater linkInfoUpdater) {
        this.linkRepository = linkRepository;
        this.linkInfoUpdater = linkInfoUpdater;
    }
}
