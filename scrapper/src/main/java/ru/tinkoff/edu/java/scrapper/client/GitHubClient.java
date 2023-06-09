package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.response.client.GitHubResponse;

import java.util.Optional;

public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient(String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Optional<GitHubResponse> fetchRepository(String user, String repo) {
        return webClient.get()
                .uri("/repos/{user}/{repo}", user, repo)
                .retrieve()
                .bodyToMono(GitHubResponse.class)
                .blockOptional();
    }
}
