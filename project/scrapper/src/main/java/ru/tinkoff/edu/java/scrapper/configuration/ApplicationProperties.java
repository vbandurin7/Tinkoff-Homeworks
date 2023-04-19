package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Import(ClientConfiguration.class)
public record ApplicationProperties(@NotNull String test, Scheduler scheduler, @NotNull Long checkInterval) {
    record Scheduler(Duration interval) {}
}