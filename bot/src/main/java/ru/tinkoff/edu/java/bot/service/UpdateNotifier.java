package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class UpdateNotifier {

    private final TelegramBot bot;

    public void updateNotify(List<Long> tgChatIds, String message) {
        for (Long tgChatId : tgChatIds) {
            bot.execute(new SendMessage(tgChatId, message).parseMode(ParseMode.HTML));
        }
    }
}
