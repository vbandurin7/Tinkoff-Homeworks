package ru.tinkoff.edu.java.scrapper.persistence.service;

import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.repository.SubscriptionRepository;

import java.util.List;

public abstract class AbstractSubscriptionService implements SubscriptionService {
    protected LinkService linkService;
    protected ChatService chatService;
    protected SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public LinkDto addLink(long tgChatId, String url) {
        LinkSaveRequest lr = new LinkSaveRequest(new LinkDto(url),
                                                 new ru.tinkoff.edu.java.scrapper.persistence.entity.Link());
        linkService.save(lr);
        chatService.register(new ChatSaveRequest(new ChatDto(tgChatId),
                                                 new ru.tinkoff.edu.java.scrapper.persistence.entity.Chat()));
        if (!inRelation(tgChatId, lr.getDtoLink())) {
            subscriptionRepository.addRelation(tgChatId, lr.getDtoLink().getUrl());
        }
        return lr.getDtoLink();
    }

    @Override
    @Transactional
    public LinkDto removeLink(long tgChatId, String url) {
        LinkDto linkDto = linkService.findByUrl(url);
        if (linkDto == null) {
            throw new LinkNotFoundException();
        }
        subscriptionRepository.deleteRelation(tgChatId, linkDto.getUrl());
        if (countLinkTracks(linkDto.getUrl()) == 0) {
            linkService.delete(linkDto.getUrl());
        }
        if (countChatTracks(tgChatId) == 0) {
            chatService.unregister(tgChatId);
        }
        return linkDto;
    }

    @Override
    public List<LinkDto> listAll(long tgChatId) {
        return subscriptionRepository.findAllByChat(tgChatId);
    }

    @Override
    public List<ChatDto> chatList(String url) {
        return subscriptionRepository.findChatsByLink(url);
    }

    private long countLinkTracks(String linkUrl) {
        return subscriptionRepository.countLinkTracks(linkUrl);
    }

    private long countChatTracks(long tgChatId) {
        return subscriptionRepository.countChatTracks(tgChatId);
    }

    private boolean inRelation(long tgChatId, LinkDto linkDto) {
        return subscriptionRepository.findAllByChat(tgChatId).contains(linkDto);
    }

}
