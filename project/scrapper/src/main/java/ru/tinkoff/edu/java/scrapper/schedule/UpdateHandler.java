package ru.tinkoff.edu.java.scrapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.linkParser.parserResult.GitHubResult;
import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;
import ru.tinkoff.edu.java.linkParser.parserResult.StackOverflowResult;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.response.client.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowResponse;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static ru.tinkoff.edu.java.scrapper.schedule.utils.SchedulerUtils.*;

@Component
@RequiredArgsConstructor
public class UpdateHandler {
    private final LinkService linkService;
    private final GitHubClient gitHubClient;
    private final StackoverflowClient stackoverflowClient;
    private final BotClient botClient;
    public void handleUpdate(Link link, ParseResult parseResult) {
        if (parseResult instanceof GitHubResult pr) {
            Optional<GitHubResponse> gitHubResponse = gitHubClient.fetchRepository(pr.name(), pr.repository());
            updateLink(gitHubResponse, getGitHubFunction(), link);
        } else if (parseResult instanceof StackOverflowResult pr) {
            Optional<StackoverflowResponse> stackoverflowResponse = stackoverflowClient.fetchQuestion(Long.parseLong(pr.id()));
            updateLink(stackoverflowResponse, getStackoverflowFunction(), link);
        }
        botClient.postUpdate(link.getId());
    }
    private <T> void updateLink(Optional<T> response, Function<T, OffsetDateTime> f, Link link) {
        if (response.isPresent() &&
                !Objects.equals(f.apply(response.get()), timestampToOffset(linkService, link))) {
            link.setUpdatedAt(Timestamp.from(f.apply(response.get()).toInstant()));
            linkService.updateTime(link);
        }
    }
}
