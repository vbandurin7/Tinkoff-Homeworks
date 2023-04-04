package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;

import java.net.URI;
import java.util.List;

@RestController
public class ScrapperLinksController {

    @GetMapping(value = "/links", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ListLinksResponse> getLinks(@Valid @NotNull @RequestHeader("Tg-Chat-Id") Long tgChatId) {
        // throw TypeMismatchException
        return ResponseEntity.ok(new ListLinksResponse(List.of(new LinkResponse(1, URI.create("todo"))), 1));
    }

    @PostMapping(value = "/links", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") @NotNull Long id,
                                                @RequestBody @NotNull AddLinkRequest addLinkRequest) {
        // если некорректные параметры запроса, опять же обработается в AdviceController
        return ResponseEntity.ok(new LinkResponse(id, URI.create(addLinkRequest.uri())));
    }

    @DeleteMapping(value = "/links", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LinkResponse> deleteLink(@RequestHeader("Tg-Chat-Id") @NotNull Long id,
                                                @RequestBody @NotNull RemoveLinkRequest removeLinkRequest) {
        // если некорректные параметры запроса, опять же обработается в AdviceController
        return ResponseEntity.ok(new LinkResponse(id, URI.create(removeLinkRequest.link())));
    }
}
