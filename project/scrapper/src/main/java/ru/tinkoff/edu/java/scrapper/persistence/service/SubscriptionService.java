package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.net.URI;
import java.util.Collection;

public interface SubscriptionService {
    Link addLink(long tgChatId, URI url);
    Link removeLink(long tgChatId, URI url);

    Collection<Link> listAll(long tgChatId);

}