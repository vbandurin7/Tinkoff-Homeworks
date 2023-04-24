package ru.tinkoff.edu.java.scrapper.dto.response.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record StackoverflowResponse(@JsonProperty("title") String title,
                                    @JsonProperty("last_activity_date") OffsetDateTime lastEditDate,
                                    @JsonProperty("answer_count") Long answerCount) implements ClientResponse {
}
