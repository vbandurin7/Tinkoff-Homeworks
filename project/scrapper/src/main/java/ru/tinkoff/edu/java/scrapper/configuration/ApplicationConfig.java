package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;
import reactor.core.scheduler.Scheduler;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Import(ClientConfiguration.class)
public record ApplicationConfig(@NotNull String test, Scheduler scheduler) {
//    @Value("${app.scheduler.interval}")
//    private static Duration schedulerInterval;
}