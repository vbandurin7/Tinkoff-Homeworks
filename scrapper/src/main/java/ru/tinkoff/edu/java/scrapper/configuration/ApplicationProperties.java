package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.configuration.enums.AccessType;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationProperties(@NotNull String test, Scheduler scheduler, @NotNull Duration checkInterval,
                                    @NotNull AccessType databaseAccessType,
                                    @NotNull String queueName, @NotNull String exchangeName,
                                    boolean useQueue) {
    record Scheduler(Duration interval) {}
}
