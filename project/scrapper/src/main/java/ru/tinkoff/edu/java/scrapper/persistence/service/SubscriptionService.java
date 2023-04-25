package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.persistence.dto.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;

import java.util.List;

public interface SubscriptionService {
    Link addLink(long tgChatId, String url);
    Link removeLink(long tgChatId, String url);
    List<Chat> chatList(String url);
    List<Link> listAll(long tgChatId);

}