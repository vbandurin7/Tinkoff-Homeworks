package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.bot.TelegramBotCommandListener;
import ru.tinkoff.edu.java.bot.bot.command.commandHandler.GlobalCommandHandler;

@Configuration
@EnableConfigurationProperties(ApplicationConfig.class)
@RequiredArgsConstructor
public class BotConfiguration {

    private final ApplicationConfig applicationProperties;
    @Bean("telegramBot")
    public TelegramBot telegramBot(GlobalCommandHandler globalCommandHandler) {
        TelegramBot tgBot = new TelegramBot(applicationProperties.botToken());
        TelegramBotCommandListener telegramBotCommandListener = new TelegramBotCommandListener(tgBot, globalCommandHandler);
        tgBot.setUpdatesListener(telegramBotCommandListener);
        tgBot.execute(new SetMyCommands(telegramBotCommandListener.getCommands().toArray(new BotCommand[0])));
        return tgBot;
    }
}
