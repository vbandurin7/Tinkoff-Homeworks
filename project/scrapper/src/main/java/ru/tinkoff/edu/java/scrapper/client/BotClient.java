package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.persistence.service.rabbitmq.UpdateSender;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class BotClient implements UpdateSender {

    private static final String BOT_BASE_URL = "http://localhost:8081";
    private final WebClient webClient;

    public BotClient() {
        this(BOT_BASE_URL);
    }

    public BotClient(String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public void postUpdate(LinkUpdateRequest linkUpdateRequest) {
        webClient.post()
                .uri("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(linkUpdateRequest)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
