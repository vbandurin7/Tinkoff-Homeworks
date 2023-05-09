package ru.tinkoff.edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.commandHandler.GlobalCommandHandler;
import ru.tinkoff.edu.java.bot.bot.command.processor.CommandProcessor;

import java.util.Collection;
import java.util.List;


@RequiredArgsConstructor
@Component
public class TelegramBotCommandListener implements Bot {
    private final TelegramBot telegramBot;
    private final GlobalCommandHandler commandProcessor;


    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            SendMessage msg = commandProcessor.handle(update);
            telegramBot.execute(msg);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public List<BotCommand> getCommands() {
        return commandProcessor.getCommandProcessors()
                .stream()
                .map(CommandProcessor::toApiCommand)
                .toList();
    }
}
