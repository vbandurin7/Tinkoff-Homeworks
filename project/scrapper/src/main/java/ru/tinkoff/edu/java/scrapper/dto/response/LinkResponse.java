package ru.tinkoff.edu.java.scrapper.dto.response;

import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.net.URI;

public record LinkResponse(long id, URI url) {
    public static LinkResponse create(Link link) {
        return new LinkResponse(link.getId(), URI.create(link.getUrl()));
    }
}
