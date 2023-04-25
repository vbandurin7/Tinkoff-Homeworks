package ru.tinkoff.edu.java.scrapper.persistence.service;

import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.SubscriptionRepository;

import java.util.List;

public abstract class AbstractSubscriptionService implements SubscriptionService {
    protected LinkService jdbcLinkService;
    protected ChatService jdbcChatService;
    protected SubscriptionRepository subscriptionRepository;


    @Override
    @Transactional
    public Link addLink(long tgChatId, String url) {
        Link link = new Link(url);
        jdbcLinkService.save(link);
        jdbcChatService.register(new Chat(tgChatId));
        if (!inRelation(tgChatId, link)) {
            subscriptionRepository.addRelation(tgChatId, link.getUrl());
        }
        return link;
    }

    @Override
    @Transactional
    public Link removeLink(long tgChatId, String url) {
        Link link = jdbcLinkService.findByUrl(url);
        if (link == null) {
            throw new LinkNotFoundException();
        }
        subscriptionRepository.deleteRelation(tgChatId, link.getUrl());
        if (countLinkTracks(link.getUrl()) == 0) {
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
    @Override
    public List<Chat> chatList(String url) {
        return subscriptionRepository.findChatsByLink(url);
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
