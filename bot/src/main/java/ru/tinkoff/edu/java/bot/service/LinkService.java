package ru.tinkoff.edu.java.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinksResponse;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public final class LinkService {

    private final ScrapperClient scrapperClient;

    public Optional<LinkResponse> track(long tgChatId, String link) {
        return Optional.ofNullable(scrapperClient.addLink(tgChatId, link).getBody());
    }

    public Optional<LinkResponse> untrack(long tgChatId, String link) {
        return Optional.ofNullable(scrapperClient.deleteLink(tgChatId, link).getBody());
    }

    public Optional<ListLinksResponse> getLinksList(long id) {
        return Optional.ofNullable(scrapperClient.getLinks(id).getBody());
    }
}
