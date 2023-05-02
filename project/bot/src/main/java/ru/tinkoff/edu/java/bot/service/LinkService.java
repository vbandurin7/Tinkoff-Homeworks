package ru.tinkoff.edu.java.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinksResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class LinkService {

    public Optional<LinkResponse> track(String link) {
        return Optional.empty();
    }

    public Optional<LinkResponse> untrack(String link) {
        return Optional.empty();
    }

    public Optional<ListLinksResponse> getLinksList() {
        return Optional.empty();
    }

}
