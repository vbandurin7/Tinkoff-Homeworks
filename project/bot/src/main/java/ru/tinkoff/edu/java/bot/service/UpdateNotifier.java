package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class UpdateNotifier {
    private final TelegramBot bot;
    public void updateNotify(List<Long> tgChatIds, String message) {
        SendMessage msg;
        for (Long tgChatId : tgChatIds) {
            msg = new SendMessage(tgChatId, message);
            bot.execute(msg);
        }
    }
}
