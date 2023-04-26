package ru.tinkoff.edu.java.scrapper.persistence.service;

import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.SubscriptionRepository;

import java.util.List;

public abstract class AbstractSubscriptionService implements SubscriptionService {
    protected LinkService linkService;
    protected ChatService chatService;
    protected SubscriptionRepository subscriptionRepository;


    @Override
    @Transactional
    public Link addLink(long tgChatId, String url) {
        LinkSaveRequest lr = new LinkSaveRequest(new Link(url), new ru.tinkoff.edu.java.scrapper.persistence.entity.Link());
        linkService.save(lr);
        chatService.register(new ChatSaveRequest(new Chat(tgChatId), new ru.tinkoff.edu.java.scrapper.persistence.entity.Chat()));
        if (!inRelation(tgChatId, lr.getDtoLink())) {
            subscriptionRepository.addRelation(tgChatId, lr.getDtoLink().getUrl());
        }
        return lr.getDtoLink();
    }

    @Override
    @Transactional
    public Link removeLink(long tgChatId, String url) {
        Link link = linkService.findByUrl(url);
        if (link == null) {
            throw new LinkNotFoundException();
        }
        subscriptionRepository.deleteRelation(tgChatId, link.getUrl());
        if (countLinkTracks(link.getUrl()) == 0) {
            linkService.delete(link.getUrl());
        }
        if (countChatTracks(tgChatId) == 0) {
            chatService.unregister(tgChatId);
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
