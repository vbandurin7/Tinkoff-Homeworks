package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowResponse;

@RequiredArgsConstructor
public class StackoverflowClient {
    private final WebClient webClient;

    public StackoverflowClient() {
        this.webClient = WebClient.builder().
                baseUrl("https://stackoverflow.com").
                build();
    }

    public StackoverflowClient(String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url == null ? "https://stackoverflow.com" : url)
                .build();
    }

    public Mono<StackoverflowResponse> fetchQuestion(long id) {
        return webClient.get()
                .uri("/questions/{id}", id)
                .retrieve()
                .bodyToMono(StackoverflowResponse.class);
    }
}
