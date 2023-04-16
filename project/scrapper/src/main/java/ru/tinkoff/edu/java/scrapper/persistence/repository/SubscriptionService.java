package ru.tinkoff.edu.java.scrapper.persistence.repository;

import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.net.URI;

public interface SubscriptionService {

    Link addLink(long tgChatId, URI url);

    Link removeLink(long tgChatId, URI url);

}
