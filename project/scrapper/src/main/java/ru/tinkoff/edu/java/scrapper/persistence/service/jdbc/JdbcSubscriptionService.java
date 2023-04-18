package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.SubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.SubscriptionService;


import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcSubscriptionService implements SubscriptionService {
    private final JdbcLinkService jdbcLinkService;
    private final JdbcChatService jdbcChatService;
    private final SubscriptionRepository subscriptionRepository;


    @Override
    @Transactional
    public Link addLink(long tgChatId, URI url) {
        Link link = new Link(url);
        jdbcLinkService.save(link);
        jdbcChatService.register(new Chat(tgChatId));
        if (!inRelation(tgChatId, link)) {
            subscriptionRepository.addRelation(tgChatId, link.getUrl().toString());
        }
        return link;
    }

    @Override
    @Transactional
    public Link removeLink(long tgChatId, URI url) {
        Link link = jdbcLinkService.findByUrl(url.toString());
        if (link == null) {
            throw new LinkNotFoundException();
        }
        subscriptionRepository.deleteRelation(tgChatId, link.getUrl().toString());
        if (countLinkTracks(link.getUrl().toString()) == 0) {
            jdbcLinkService.delete(link.getUrl());
        }
        if (countChatTracks(tgChatId) == 0) {
            jdbcChatService.unregister(tgChatId);
        }
        return link;
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return subscriptionRepository.findAllByChat(tgChatId);
    }

    private long countLinkTracks(String linkUrl) {
        return subscriptionRepository.countLinkTracks(linkUrl);
    }

    private long countChatTracks(long tgChatId) {
        return subscriptionRepository.countChatTracks(tgChatId);
    }

    private boolean inRelation(long tgChatId, Link link) {
        return subscriptionRepository.findAllByChat(tgChatId).contains(link);
    }


}
