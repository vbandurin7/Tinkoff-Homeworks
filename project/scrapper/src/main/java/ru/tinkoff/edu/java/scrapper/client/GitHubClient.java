package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.response.client.GitHubResponse;

public class GitHubClient {
    private final WebClient webClient;

    public GitHubClient() {
        this.webClient = WebClient.builder().
                baseUrl("https://github.com").
                build();
    }

    public GitHubClient(String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url == null ? "https://github.com" : url)
                .build();
    }

    public Mono<GitHubResponse> fetchRepository(String user, String repo) {
        return webClient.get()
                .uri("/{user}/{repo}", user, repo)
                .retrieve()
                .bodyToMono(GitHubResponse.class);
    }
}
