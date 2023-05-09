package ru.tinkoff.edu.java.scrapper.dto.response;

import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {
    public static ListLinksResponse create(List<LinkDto> linkDtos) {
        return new ListLinksResponse(
                linkDtos.stream().map(LinkResponse::create).toList(),
                linkDtos.size()
        );
    }
}
