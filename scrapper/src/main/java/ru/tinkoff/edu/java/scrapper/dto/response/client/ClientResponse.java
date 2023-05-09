package ru.tinkoff.edu.java.scrapper.dto.response.client;

public sealed interface ClientResponse permits GitHubResponse, StackoverflowResponse {
}
