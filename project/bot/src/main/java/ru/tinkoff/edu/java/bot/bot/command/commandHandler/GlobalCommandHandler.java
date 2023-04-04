package ru.tinkoff.edu.java.bot.bot.command.commandHandler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.processor.CommandProcessor;


import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public final class GlobalCommandHandler {
    private final List<CommandProcessor> commandProcessors;

    public SendMessage handle(Update update) {
        Optional<? extends CommandProcessor> first = commandProcessors.stream()
                .filter(processor -> processor.supports(update))
                .findFirst();
        return first.isPresent() ? first.get().handle(update) : unsupportedCommand(update);
    }

    private SendMessage unsupportedCommand(Update update) {
        return new SendMessage(update.message().chat().id(), "Unknown command. Try '/help'");
    }

}
