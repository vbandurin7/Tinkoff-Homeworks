package ru.tinkoff.edu.java.bot.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateResponse;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
public class BotController {

    @PostMapping(value = "/updates", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LinkUpdateResponse> postUpdate(@RequestBody @NotNull LinkUpdateRequest linkUpdateRequest) {
        // тут может быть какая-то ошибка, (пусть пока IllegalArgumentException) поэтому добавим обработчик в AdviceController
        return ResponseEntity.ok(new LinkUpdateResponse(UUID.randomUUID().toString()));
    }
}
