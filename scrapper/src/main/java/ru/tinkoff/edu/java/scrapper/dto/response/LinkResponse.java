package ru.tinkoff.edu.java.scrapper.dto.response;

import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;

import java.net.URI;

public record LinkResponse(long id, URI url) {
    public static LinkResponse create(LinkDto linkDto) {
        return new LinkResponse(linkDto.getId(), URI.create(linkDto.getUrl()));
    }
}
