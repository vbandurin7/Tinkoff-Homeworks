package ru.tinkoff.edu.java.scrapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.linkParser.parserResult.GitHubResult;
import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;
import ru.tinkoff.edu.java.linkParser.parserResult.StackOverflowResult;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.client.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowResponse;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.service.LinkService;
import ru.tinkoff.edu.java.scrapper.persistence.service.SubscriptionService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static ru.tinkoff.edu.java.scrapper.schedule.utils.SchedulerUtils.*;

@Component
@RequiredArgsConstructor
public class UpdateHandler {
    private final LinkService linkService;
    private final SubscriptionService subscriptionService;
    private final GitHubClient gitHubClient;
    private final StackoverflowClient stackoverflowClient;
    private final BotClient botClient;
    public void handleUpdate(Link link, ParseResult parseResult) {
        String description = "";
        if (parseResult instanceof GitHubResult pr) {
            Optional<GitHubResponse> gitHubResponse = gitHubClient.fetchRepository(pr.name(), pr.repository());
            updateLink(gitHubResponse, getGitHubFunction(), link);
            description = "User %s has pushed a new commit at repository %s".formatted(pr.name(), pr.repository());
        } else if (parseResult instanceof StackOverflowResult pr) {
            Optional<StackoverflowResponse> stackoverflowResponse = stackoverflowClient.fetchQuestion(Long.parseLong(pr.id()));
            updateLink(stackoverflowResponse, getStackoverflowFunction(), link);
            description = "New answers to the question with ID %s were left".formatted(pr.id());
        }
        botClient.postUpdate(new LinkUpdateRequest(link.getId(), link.getUrl().toString(), description,
                subscriptionService.chatList(link.getUrl().toString()).stream().map(Chat::getId).toList()));
    }
    private <T> void updateLink(Optional<T> response, Function<T, OffsetDateTime> f, Link link) {
        if (response.isPresent() &&
                !Objects.equals(f.apply(response.get()), timestampToOffset(linkService, link))) {
            link.setUpdatedAt(Timestamp.from(f.apply(response.get()).toInstant()));
            link.setLastCheckedAt(Timestamp.from(Instant.now()));
            linkService.updateTime(link);
        }
    }
}
