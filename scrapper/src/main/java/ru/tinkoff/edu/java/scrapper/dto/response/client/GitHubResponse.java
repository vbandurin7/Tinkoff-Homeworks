package ru.tinkoff.edu.java.scrapper.dto.response.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GitHubResponse(@JsonProperty("full_name") String fullName, @JsonProperty("pushed_at") OffsetDateTime updatedAt) implements ClientResponse {
}
