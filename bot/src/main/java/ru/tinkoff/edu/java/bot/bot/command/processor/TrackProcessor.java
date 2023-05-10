package ru.tinkoff.edu.java.bot.bot.command.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.validator.CommandValidator;
import ru.tinkoff.edu.java.bot.service.LinkService;

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
        if (linkService.track(update.message().chat().id(), argList.get().get(0)).isEmpty()) {
            send(update, String.format("Unable to start tracking link %s", argList.get().get(0)));
        }
        return send(update, String.format("link %s was added for tracking successfully", argList.get().get(0)));
    }
}
