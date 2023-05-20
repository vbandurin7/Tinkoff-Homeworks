package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.SubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.EntityConverter;
import ru.tinkoff.edu.java.scrapper.persistence.service.utils.LinkInfoUpdater;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaSubscriptionService implements SubscriptionService {

    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;
    private final LinkInfoUpdater linkInfoUpdater;

    @Override
    @Transactional
    public LinkDto addLink(long tgChatId, String url) {
        Link entityLink;
        Optional<Chat> optionalChat;
        Chat entityChat;
        try {
            optionalChat = chatRepository.findById(tgChatId);
            entityChat = optionalChat.orElseGet(() -> new Chat(tgChatId));
        } catch (JpaObjectRetrievalFailureException e) {
            entityChat = new Chat(tgChatId);
        }
        entityLink = linkRepository.findByUrl(url);
        LinkSaveRequest linkSaveRequest = new LinkSaveRequest(new LinkDto(url), entityLink);
        ChatSaveRequest chatSaveRequest = new ChatSaveRequest(new ChatDto(tgChatId), entityChat);
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
    public LinkDto removeLink(long tgChatId, String url) {
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
        if (entityChat.getLinks().isEmpty()) {
            chatRepository.deleteById(entityChat.getId());
        } else {
            chatRepository.save(entityChat);
        }
        if (entityLink.getChats().isEmpty()) {
            linkRepository.deleteByUrl(entityLink.getUrl());
        } else {
            linkRepository.save(entityLink);
        }
        return EntityConverter.entityToDtoLink(entityLink);
    }

    @Override
    @Transactional
    public List<ChatDto> chatList(String url) {
        var entityLink = linkRepository.findByUrl(url);
        if (entityLink == null) {
            return Collections.emptyList();
        }
        return entityLink.getChats().stream().map(EntityConverter::entityToDtoChat).toList();
    }

    @Override
    @Transactional
    public List<LinkDto> listAll(long tgChatId) {
        var optionalChat = chatRepository.findById(tgChatId);
        var entityChat = optionalChat.orElse(null);
        if (entityChat == null) {
            return Collections.emptyList();
        }
        return entityChat.getLinks().stream().map(EntityConverter::entityToDtoLink).toList();
    }
}
