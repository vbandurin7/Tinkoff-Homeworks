package ru.tinkoff.edu.java.bot.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.validator.CommandValidatorImpl;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.service.LinkService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public final class ListProcessor extends AbstractCommandProcessor{

    private final CommandValidatorImpl commandValidator;
    private final LinkService linkService;

    @Override
    public String command() {
        return Commands.LIST.getCommand();
    }

    @Override
    public String description() {
        return Commands.LIST.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {

        if (commandValidator.validateCommand(0, update.message().text()).isEmpty()) {
            return send(update, "Wrong number of arguments.");
        }

        Optional<List<LinkResponse>> linkList = linkService.getLinksList();
        if (linkList.isEmpty()) {
            // todo
            // тут мб какие-то ошибки будут, но обработку пока не буду добавлять, непонятно что именно может быть
        }
        return send(update, createMessage(List.of(
                new LinkResponse(1, "test-link1"),
                new LinkResponse(2, "test-link2"))));
    }

    private String createMessage(List<LinkResponse> linkResponses) {
        return "All tracking links are below: " + System.lineSeparator().repeat(2) +
                linkResponses.stream()
                .map(LinkResponse::link)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
