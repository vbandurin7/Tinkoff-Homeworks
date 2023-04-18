package ru.tinkoff.edu.java.scrapper.schedule.utils;

import ru.tinkoff.edu.java.scrapper.dto.response.client.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowResponse;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.function.Function;

public class SchedulerUtils {
    public static OffsetDateTime timestampToOffset(LinkService linkService, Link link) {
        return OffsetDateTime.ofInstant(linkService.findByUrl(link.getUrl().toString()).getUpdatedAt().toInstant(), ZoneId.of("UTC"));
    }

    public static Function<GitHubResponse, OffsetDateTime> getGitHubFunction() {
        return GitHubResponse::updatedAt;
    }

    public static Function<StackoverflowResponse, OffsetDateTime> getStackoverflowFunction() {
        return StackoverflowResponse::lastEditDate;
    }
}
