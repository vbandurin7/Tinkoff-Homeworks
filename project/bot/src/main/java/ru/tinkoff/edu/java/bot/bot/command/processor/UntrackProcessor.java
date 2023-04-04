package ru.tinkoff.edu.java.bot.bot.command.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.commands.Command;
import ru.tinkoff.edu.java.bot.bot.command.processor.AbstractCommandProcessor;
import ru.tinkoff.edu.java.bot.bot.command.validator.CommandValidatorImpl;
import ru.tinkoff.edu.java.bot.service.LinkService;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public final class UntrackProcessor extends AbstractCommandProcessor {
    private final CommandValidatorImpl commandValidator;
    private final LinkService linkService;

    @Override
    public String command() {
        return Command.UNTRACK.getCommand();
    }

    @Override
    public String description() {
        return Command.UNTRACK.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        Optional<List<String>> argList;
        if ((argList = commandValidator.validateCommand(1, update.message().text())).isEmpty()) {
            return send(update, "Wrong number of arguments.");
        }
        if (linkService.untrack(argList.get().get(0)).isEmpty()) {
            send(update, "Unable to stop tracking link " + argList.get().get(0));
        }
        return send(update, ("link " + argList.get().get(0) + " is no longer tracked"));
    }
}
