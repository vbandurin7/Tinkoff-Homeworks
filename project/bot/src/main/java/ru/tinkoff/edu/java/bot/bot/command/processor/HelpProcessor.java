package ru.tinkoff.edu.java.bot.bot.command.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.commands.Command;
import ru.tinkoff.edu.java.bot.bot.command.processor.AbstractCommandProcessor;
import ru.tinkoff.edu.java.bot.bot.command.processor.CommandProcessor;
import ru.tinkoff.edu.java.bot.bot.command.validator.CommandValidatorImpl;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public final class HelpProcessor extends AbstractCommandProcessor {

    private final List<CommandProcessor> commandProcessors;
    private final CommandValidatorImpl commandValidator;


    @Override
    public String command() {
        return Command.HELP.getCommand();
    }

    @Override
    public String description() {
        return Command.HELP.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        if (commandValidator.validateCommand(0, update.message().text()).isEmpty()) {
            return send(update, "No arguments expected");
        }
        return send(update, createMessage());
    }

    private String createMessage() {
        return commandProcessors.stream()
                .map(Object::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
