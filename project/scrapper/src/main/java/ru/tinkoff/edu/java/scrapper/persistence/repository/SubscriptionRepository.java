package ru.tinkoff.edu.java.scrapper.persistence.repository;

import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.util.List;

public interface SubscriptionRepository {
    void addRelation(long tgChatId, String linkUrl);
    void deleteRelation(long tgChatId, String linkUrl);
    List<Link> findAllByChat(long tgChatId);

    long countLinkTracks(String linkUrl);

    long countChatTracks(Long id);
}
