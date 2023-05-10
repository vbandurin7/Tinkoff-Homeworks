package ru.tinkoff.edu.java.scrapper.schedule.utils;

import ru.tinkoff.edu.java.scrapper.dto.response.client.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowResponse;
import java.time.OffsetDateTime;
import java.util.function.Function;

public final class SchedulerUtils {

    private SchedulerUtils() {
    }

    public static Function<GitHubResponse, OffsetDateTime> getGitHubFunction() {
        return GitHubResponse::updatedAt;
    }

    public static Function<StackoverflowResponse, OffsetDateTime> getStackoverflowFunction() {
        return StackoverflowResponse::lastEditDate;
    }
}
