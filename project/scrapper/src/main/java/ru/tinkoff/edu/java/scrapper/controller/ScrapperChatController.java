package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.response.RegisterChatResponse;
import ru.tinkoff.edu.java.scrapper.exception.ChatNotFoundException;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
public class ScrapperChatController {

    @PostMapping(value = "/tg-chat/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RegisterChatResponse> registerChat(@PathVariable("id") @NotNull Long id) {
        // Если передадут неверный id, выкинет TypeMismatchException, добавим обработку в AdviceController
        if (id == null) {
            throw new ChatNotFoundException();
        }
        return ResponseEntity.ok(new RegisterChatResponse("Chat with id " + id + " was registered successfully"));
    }

    @DeleteMapping(value = "/tg-chat/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RegisterChatResponse> deleteChat(@PathVariable("id") @NotNull Long id) {
        // Если передадут неверный id, выкинет TypeMismatchException, добавим обработку в AdviceController
        // либо если чата не существует, прокинем ChatNotFoundException
        return ResponseEntity.ok(new RegisterChatResponse("Chat with id " + id + " was deleted successfully"));
    }


}
