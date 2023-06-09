package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.request.ChatSaveRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.DeleteChatResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.RegisterChatResponse;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.service.ChatService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@RequiredArgsConstructor
public class ScrapperChatController {

    private final ChatService chatService;

    @PostMapping(value = "/tg-chat/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RegisterChatResponse> registerChat(@PathVariable("id") @NotNull Long id) {
        chatService.register(new ChatSaveRequest(
            new ChatDto(id),
            new Chat(id)
        ));
        return ResponseEntity.ok(new RegisterChatResponse("Chat with id " + id + " was registered successfully"));
    }

    @DeleteMapping(value = "/tg-chat/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DeleteChatResponse> deleteChat(@PathVariable("id") @NotNull Long id) {
        chatService.unregister(id);
        return ResponseEntity.ok(new DeleteChatResponse("Chat with id " + id + " was deleted successfully"));
    }
}
