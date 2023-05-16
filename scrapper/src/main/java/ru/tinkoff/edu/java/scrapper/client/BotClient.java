package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.client.LinkUpdateResponse;
import ru.tinkoff.edu.java.scrapper.persistence.service.rabbitmq.UpdateSender;

public class BotClient implements UpdateSender {

    private final WebClient webClient;

    public BotClient(String url) {
        this.webClient = WebClient.builder()
            .baseUrl(url)
            .build();
    }

    public ResponseEntity<LinkUpdateResponse> postUpdate(LinkUpdateRequest linkUpdateRequest) {
        return webClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(linkUpdateRequest)
            .retrieve()
            .toEntity(LinkUpdateResponse.class)
            .block();
    }

    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        webClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(linkUpdateRequest)
            .retrieve()
            .toBodilessEntity()
            .block();
    }
}
