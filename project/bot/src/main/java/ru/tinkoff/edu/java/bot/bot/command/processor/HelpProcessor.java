package ru.tinkoff.edu.java.bot.bot.command.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.validator.CommandValidator;

import java.util.List;
import java.util.stream.Collectors;

import static ru.tinkoff.edu.java.bot.bot.command.commands.Command.HELP;

@Component
@RequiredArgsConstructor
public final class HelpProcessor extends AbstractCommandProcessor {

    private final List<CommandProcessor> commandProcessors;
    private final CommandValidator commandValidator;


    @Override
    public String command() {
        return HELP.getCommand();
    }

    @Override
    public String description() {
        return HELP.getDescription();
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
