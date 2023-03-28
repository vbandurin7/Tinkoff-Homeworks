package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowItemResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowResponse;

import java.util.Optional;

public class StackoverflowClient {
    private final static String STACKOVERFLOW_BASE_URL = "https://api.stackexchange.com/2.3";
    private final WebClient webClient;

    public StackoverflowClient() {
        this.webClient = WebClient.builder().
                baseUrl(STACKOVERFLOW_BASE_URL).
                build();
    }

    public StackoverflowClient(String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Optional<StackoverflowResponse> fetchQuestion(long id) {
        return webClient.get()
                .uri("/questions/{id}?site=stackoverflow", id)
                .retrieve()
                .bodyToMono(StackoverflowItemResponse.class)
                .blockOptional()
                .filter(itemResponse -> !itemResponse.items().isEmpty())
                .map(questions -> questions.items().get(0));
    }
}
