package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.service.SubscriptionService;


import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScrapperLinksController {

    private final SubscriptionService subscriptionService;

    @GetMapping(value = "/links", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ListLinksResponse> getLinks(@Valid @NotNull @RequestHeader("Tg-Chat-Id") Long tgChatId) {
        List<LinkDto> linkDtos = subscriptionService.listAll(tgChatId);
        return ResponseEntity.ok(ListLinksResponse.create(linkDtos));
    }

    @PostMapping(value = "/links",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") @NotNull Long id,
                                                @RequestBody @NotNull AddLinkRequest addLinkRequest) {
        LinkDto linkDto = subscriptionService.addLink(id, addLinkRequest.uri());
        return ResponseEntity.ok(new LinkResponse(linkDto.getId(), URI.create(linkDto.getUrl())));
    }

    @DeleteMapping(value = "/links",
                   consumes = MediaType.APPLICATION_JSON_VALUE,
                   produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LinkResponse> deleteLink(@RequestHeader("Tg-Chat-Id") @NotNull Long id,
                                                   @RequestBody @NotNull RemoveLinkRequest removeLinkRequest) {
        LinkDto linkDto = subscriptionService.removeLink(id, removeLinkRequest.link());
        return ResponseEntity.ok(new LinkResponse(linkDto.getId(), URI.create(linkDto.getUrl())));
    }
}
