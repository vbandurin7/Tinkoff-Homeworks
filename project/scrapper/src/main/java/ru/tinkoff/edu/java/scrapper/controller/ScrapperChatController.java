package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.response.DeleteChatResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.RegisterChatResponse;
import ru.tinkoff.edu.java.scrapper.exception.ChatAlreadyExistsException;
import ru.tinkoff.edu.java.scrapper.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.service.jdbc.JdbcChatService;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@RequiredArgsConstructor
public class ScrapperChatController {

    private final JdbcChatService jdbcChatService;

    @PostMapping(value = "/tg-chat/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RegisterChatResponse> registerChat(@PathVariable("id") @NotNull Long id) {
        if (id == null) {
            throw new ChatNotFoundException();
        }
        Chat chat = new Chat(id);
        if (jdbcChatService.count(chat.getId()) != 0) {
            throw new ChatAlreadyExistsException(id, "Chat already exists.");
        }
        jdbcChatService.save(chat);
        return ResponseEntity.ok(new RegisterChatResponse("Chat with id " + id + " was registered successfully"));

    }

    @DeleteMapping(value = "/tg-chat/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DeleteChatResponse> deleteChat(@PathVariable("id") @NotNull Long id) {
        jdbcChatService.deleteById(id); // обработать ошибки
        return ResponseEntity.ok(new DeleteChatResponse("Chat with id " + id + " was deleted successfully"));
    }


}
