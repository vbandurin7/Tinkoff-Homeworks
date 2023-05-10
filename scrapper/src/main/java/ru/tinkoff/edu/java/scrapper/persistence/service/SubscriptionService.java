package ru.tinkoff.edu.java.scrapper.persistence.service;

import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;

import java.util.List;

public interface SubscriptionService {

    LinkDto addLink(long tgChatId, String url);

    LinkDto removeLink(long tgChatId, String url);

    List<ChatDto> chatList(String url);

    List<LinkDto> listAll(long tgChatId);

}
