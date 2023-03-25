package ru.tinkoff.edu.java.scrapper.dto.response.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record StackoverflowResponse(String title, @JsonProperty("last_edit_date") OffsetDateTime updatedAt) {
}
