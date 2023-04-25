package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.net.URI;
import java.util.List;

public interface SubscriptionService {
    Link addLink(long tgChatId, String url);
    Link removeLink(long tgChatId, String url);
    List<Chat> chatList(String url);
    List<Link> listAll(long tgChatId);

}