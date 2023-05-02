package ru.tinkoff.edu.java.scrapper.persistence.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationProperties;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.persistence.service.rabbitmq.ScrapperQueueProducer;

@Service
@RequiredArgsConstructor
public class UpdatePoster {

    private final ApplicationProperties appProps;
    private final BotClient botClient;
    private final ScrapperQueueProducer scrapperQueueProducer;

    public void sendMessage(LinkUpdateRequest linkUpdateRequest) {
        if (appProps.useQueue()) {
            scrapperQueueProducer.send(linkUpdateRequest);
        } else {
            botClient.postUpdate(linkUpdateRequest);
        }
    }
}
