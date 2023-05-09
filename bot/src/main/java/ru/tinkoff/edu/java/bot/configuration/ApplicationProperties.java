package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationProperties(@NotNull String test, String botToken, Api api,
                                    @NotNull String queueName, @NotNull String deadLetterExchangeName) {

    record Api(@NotNull String scrapperBaseUrl) {}
}
