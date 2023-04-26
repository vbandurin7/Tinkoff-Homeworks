package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.SubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.EntityConverter;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.LinkInfoUpdater;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public class JpaSubscriptionService implements SubscriptionService {

    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;
    private final LinkInfoUpdater linkInfoUpdater;

    @Override
    @Transactional
    public Link addLink(long tgChatId, String url) {
        var entityLink = linkRepository.findByUrl(url);
        var optionalChat = chatRepository.findById(tgChatId);
        var entityChat = optionalChat.orElseGet(() -> new ru.tinkoff.edu.java.scrapper.persistence.entity.Chat(tgChatId));
        LinkSaveRequest linkSaveRequest = new LinkSaveRequest(new Link(url), entityLink);
        ChatSaveRequest chatSaveRequest = new ChatSaveRequest(new Chat(tgChatId), entityChat);
        if (entityLink == null) {
            entityLink = new ru.tinkoff.edu.java.scrapper.persistence.entity.Link();
            linkInfoUpdater.update(linkSaveRequest);
            entityLink.setUrl(url);
            entityLink.setLinkInfo(linkSaveRequest.getDtoLink().getLinkInfo());
            entityLink.setUpdatedAt(linkSaveRequest.getDtoLink().getUpdatedAt());
            entityLink.setLastCheckedAt(OffsetDateTime.now());
        }
        entityLink.getChats().add(entityChat);
        entityChat.getLinks().add(entityLink);
        linkSaveRequest.setEntityLink(entityLink);
        chatSaveRequest.setEntityChat(entityChat);
        linkRepository.save(linkSaveRequest.getEntityLink());
        chatRepository.save(chatSaveRequest.getEntityChat());
        return linkSaveRequest.getDtoLink();
    }

    @Override
    @Transactional
    public Link removeLink(long tgChatId, String url) {
        var entityLink = linkRepository.findByUrl(url);
        if (url == null || entityLink == null) {
            throw new LinkNotFoundException("Link with url " + url + " is not tracked");
        }
        var optionalEntityChat = chatRepository.findById(tgChatId);
        if (optionalEntityChat.isEmpty()) {
            throw new ChatNotFoundException("Chat with id %d doesn't exist".formatted(tgChatId));
        }
        var entityChat = optionalEntityChat.get();
        entityChat.getLinks().remove(entityLink);
        entityLink.getChats().remove(entityChat);
        if (entityChat.getLinks().size() == 0) {
            chatRepository.deleteById(entityChat.getId());
        } else {
            chatRepository.save(entityChat);
        }
        if (entityLink.getChats().size() == 0) {
            linkRepository.deleteByUrl(entityLink.getUrl());
        } else {
            linkRepository.save(entityLink);
        }
        return EntityConverter.entityToDtoLink(entityLink);
    }

    @Override
    public List<Chat> chatList(String url) {
        var entityLink = linkRepository.findByUrl(url);
        if (entityLink == null) {
            throw new LinkNotFoundException("Link with url %s is not tracked in any chat".formatted(url));
        }
        return entityLink.getChats().stream().map(EntityConverter::entityToDtoChat).toList();
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        var optionalChat = chatRepository.findById(tgChatId);
        var entityChat = optionalChat.orElse(null);
        if (entityChat == null) {
            throw new ChatNotFoundException("Chat with id %d is not registered".formatted(tgChatId));
        }
        return entityChat.getLinks().stream().map(EntityConverter::entityToDtoLink).toList();
    }
}
