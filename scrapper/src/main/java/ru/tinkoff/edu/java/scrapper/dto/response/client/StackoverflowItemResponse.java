package ru.tinkoff.edu.java.scrapper.dto.response.client;

import java.util.List;

public record StackoverflowItemResponse(
        List<StackoverflowResponse> items
) {
}
