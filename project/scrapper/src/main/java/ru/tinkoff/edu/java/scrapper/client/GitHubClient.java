package ru.tinkoff.edu.java.scrapper.client;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.response.client.GitHubResponse;

import java.util.Optional;

@Component
public class GitHubClient {
    private final static String GITHUB_BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitHubClient() {
        this(GITHUB_BASE_URL);
    }

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
