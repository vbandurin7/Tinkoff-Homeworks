package ru.tinkoff.edu.java.scrapper.persistence.repository;

import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;

import java.util.List;

public interface SubscriptionRepository {
    void addRelation(long tgChatId, String linkUrl);
    void deleteRelation(long tgChatId, String linkUrl);
    List<LinkDto> findAllByChat(long tgChatId);

    List<ChatDto> findChatsByLink(String url);

    long countLinkTracks(String linkUrl);

    long countChatTracks(Long id);

}
