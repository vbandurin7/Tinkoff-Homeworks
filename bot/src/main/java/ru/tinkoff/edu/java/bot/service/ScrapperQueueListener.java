package ru.tinkoff.edu.java.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.request.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
@RabbitListener(queues = "${app.queue-name}")
public class ScrapperQueueListener {

    private final UpdateNotifier updateNotifier;

    @RabbitHandler
    public void receiver(LinkUpdateRequest update) {
        updateNotifier.updateNotify(update.tgChatIds(), update.description());
    }
}
