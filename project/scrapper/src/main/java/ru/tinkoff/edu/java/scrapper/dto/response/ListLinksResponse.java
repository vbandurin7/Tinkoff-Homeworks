package ru.tinkoff.edu.java.scrapper.dto.response;

import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {
    public static ListLinksResponse create(List<Link> links) {
        return new ListLinksResponse(
                links.stream().map(LinkResponse::create).toList(),
                links.size()
        );
    }
}
