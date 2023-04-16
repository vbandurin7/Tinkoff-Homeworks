package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.response.client.LinkUpdateResponse;

@RequiredArgsConstructor
public class BotClient {
    private final static String BOT_BASE_URL = "http://localhost:8081";
    private final WebClient webClient;

    public BotClient() {
        this(BOT_BASE_URL);
    }

    public BotClient(String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public ResponseEntity<LinkUpdateResponse> postUpdate(long id) {
        return webClient.post()
                .uri("/updates")
                .retrieve()
                .toEntity(LinkUpdateResponse.class)
                .block();
    }
}
