package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinksResponse;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class LinkService {
    private final TelegramBot bot;

    public Optional<LinkResponse> track(String link) {
        return Optional.empty();
    }

    public Optional<LinkResponse> untrack(String link) {
        return Optional.empty();
    }

    public Optional<ListLinksResponse> getLinksList() {
        return Optional.empty();
    }

    public void updateNotify(List<Long> tgChatIds, String message) {
        SendMessage msg;
        for (Long tgChatId : tgChatIds) {
            msg = new SendMessage(tgChatId, message);
            bot.execute(msg);
        }
    }
}
