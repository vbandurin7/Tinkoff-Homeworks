package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
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
        List<Link> links = subscriptionService.listAll(tgChatId);
        return ResponseEntity.ok(ListLinksResponse.create(links));
    }

    @PostMapping(value = "/links", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") @NotNull Long id,
                                                @RequestBody @NotNull AddLinkRequest addLinkRequest) {
        Link link = subscriptionService.addLink(id, URI.create(addLinkRequest.uri()));
        return ResponseEntity.ok(new LinkResponse(link.getId(), link.getUrl()));
    }

    @DeleteMapping(value = "/links", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LinkResponse> deleteLink(@RequestHeader("Tg-Chat-Id") @NotNull Long id,
                                                   @RequestBody @NotNull RemoveLinkRequest removeLinkRequest) {
        Link link = subscriptionService.removeLink(id, URI.create(removeLinkRequest.link()));
        return ResponseEntity.ok(new LinkResponse(link.getId(), link.getUrl()));
    }
}
