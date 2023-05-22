package ru.tinkoff.edu.java.bot.bot.command.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.validator.CommandValidator;
import ru.tinkoff.edu.java.bot.exception.ChatNotRegisteredException;
import ru.tinkoff.edu.java.bot.service.LinkService;
import ru.tinkoff.edu.java.link_parser.parser.LinkParser;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static ru.tinkoff.edu.java.bot.bot.command.commands.Command.TRACK;

@Component
@RequiredArgsConstructor
public final class TrackProcessor extends AbstractCommandProcessor {

    private final CommandValidator commandValidator;
    private final LinkService linkService;

    @Override
    public String command() {
        return TRACK.getCommand();
    }

    @Override
    public String description() {
        return TRACK.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        Optional<List<String>> argList = commandValidator.validateCommand(1, update.message().text());
        if (argList.isEmpty()) {
            return send(update, "Wrong number of arguments.");
        }

        final String link = argList.get().get(0);
        if (LinkParser.parseURL(URI.create(link)) == null) {
            return send(update, String.format("Unable to start track link %s", link));
        }
        try {
            linkService.track(update.message().chat().id(), link);
        } catch (ChatNotRegisteredException e) {
            return send(update, "To start working with links, please enter the command /start.");
        }
        return send(update, String.format("link %s was added for tracking successfully", link));
    }
}
