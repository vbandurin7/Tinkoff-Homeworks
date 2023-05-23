package ru.tinkoff.edu.java.bot.bot.command.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.validator.CommandValidator;
import ru.tinkoff.edu.java.bot.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.bot.exception.ChatNotRegisteredException;
import ru.tinkoff.edu.java.bot.service.LinkService;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.tinkoff.edu.java.bot.bot.command.commands.Command.LIST;

@Component
@RequiredArgsConstructor
public final class ListProcessor extends AbstractCommandProcessor {

    private final CommandValidator commandValidator;
    private final LinkService linkService;

    @Override
    public String command() {
        return LIST.getCommand();
    }

    @Override
    public String description() {
        return LIST.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {

        if (commandValidator.validateCommand(0, update.message().text()).isEmpty()) {
            return send(update, "Wrong number of arguments.");
        }

        Optional<ListLinksResponse> linkList;
        try {
            linkList = linkService.getLinksList(update.message().chat().id());
            return send(update, createMessage(linkList));
        } catch (ChatNotRegisteredException ex) {
            return send(update, "To start working with links, please enter the command /start.");
        }
    }

    private String createMessage(Optional<ListLinksResponse> linkResponses) {
        if (linkResponses.isEmpty() || linkResponses.get().size() == 0) {
            return "No links for track were added. Try <b>'/track [link]'</b>";
        }
        return "<b>All tracking links are below:</b> " + System.lineSeparator()
            + IntStream.range(1, linkResponses.get().size() + 1)
            .mapToObj(i -> "<b>%d)</b> ".formatted(i) + "<i>"
                + linkResponses.get().links().get(i - 1).url().toString() + "</i>")
            .collect(Collectors.joining(System.lineSeparator()));
    }
}
