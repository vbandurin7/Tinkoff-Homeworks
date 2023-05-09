package ru.tinkoff.edu.java.bot.bot.command.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.validator.CommandValidator;
import ru.tinkoff.edu.java.bot.service.LinkService;

import java.util.List;
import java.util.Optional;

import static ru.tinkoff.edu.java.bot.bot.command.commands.Command.UNTRACK;

@Component
@RequiredArgsConstructor
public final class UntrackProcessor extends AbstractCommandProcessor {
    private final CommandValidator commandValidator;
    private final LinkService linkService;

    @Override
    public String command() {
        return UNTRACK.getCommand();
    }

    @Override
    public String description() {
        return UNTRACK.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        Optional<List<String>> argList;
        if ((argList = commandValidator.validateCommand(1, update.message().text())).isEmpty()) {
            return send(update, "Wrong number of arguments.");
        }
        if (linkService.untrack(update.message().chat().id(), argList.get().get(0)).isEmpty()) {
            send(update, String.format("Unable to stop tracking link %s", argList.get().get(0)));
        }
        return send(update, String.format("link %s is no longer tracked", argList.get().get(0)));
    }
}
