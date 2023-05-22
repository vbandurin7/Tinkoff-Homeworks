package ru.tinkoff.edu.java.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.bot.bot.command.commands.Command;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.bot.exception.ChatNotRegisteredException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public final class LinkService {

    private final ScrapperClient scrapperClient;

    public Optional<LinkResponse> track(long tgChatId, String link) throws ChatNotRegisteredException {
        return executeCommand(scrapperClient.addLink(tgChatId, link), Command.TRACK.getCommand(), tgChatId);
    }

    public Optional<LinkResponse> untrack(long tgChatId, String link) throws ChatNotRegisteredException {
        return executeCommand(scrapperClient.deleteLink(tgChatId, link), Command.UNTRACK.getCommand(), tgChatId);
    }

    public Optional<ListLinksResponse> getLinksList(long id) throws ChatNotRegisteredException {
        return executeCommand(scrapperClient.getLinks(id), Command.LIST.getCommand(), id);
    }

    private <T> Optional<T> executeCommand(ResponseEntity<T> responseEntity, String command, long tgChatId)
        throws ChatNotRegisteredException {
        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new ChatNotRegisteredException("""
                Unable to complete command "%s", chat with id %d is not registered.""".formatted(command, tgChatId));
        }
        if (responseEntity.getStatusCode().is5xxServerError()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error occurred");
        }
        return Optional.ofNullable(responseEntity.getBody());
    }
}
