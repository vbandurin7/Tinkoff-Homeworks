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
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.service.SubscriptionService;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.function.Function;

import static ru.tinkoff.edu.java.scrapper.schedule.utils.SchedulerUtils.*;

@Component
@RequiredArgsConstructor
public class UpdateHandler {
    private final SubscriptionService subscriptionService;
    private final GitHubClient gitHubClient;
    private final StackoverflowClient stackoverflowClient;
    private final BotClient botClient;
    public void handleUpdate(LinkDto linkDto, ParseResult parseResult) {
        String description = "";
        if (parseResult instanceof GitHubResult pr) {
            Optional<GitHubResponse> gitHubResponse = gitHubClient.fetchRepository(pr.name(), pr.repository());
            updateLink(gitHubResponse, getGitHubFunction(), linkDto);
            description = "User %s has pushed a new commit at repository %s".formatted(pr.name(), pr.repository());
        } else if (parseResult instanceof StackOverflowResult pr) {
            Optional<StackoverflowResponse> stackoverflowResponse = stackoverflowClient.fetchQuestion(Long.parseLong(pr.id()));
            updateLink(stackoverflowResponse, getStackoverflowFunction(), linkDto);
            description = "New answers to the question with ID %s were left".formatted(pr.id());
        }
        botClient.postUpdate(new LinkUpdateRequest(linkDto.getId(), linkDto.getUrl().toString(), description,
                subscriptionService.chatList(linkDto.getUrl().toString()).stream().map(ChatDto::getId).toList()));
    }

    private <T> void updateLink(Optional<T> response, Function<T, OffsetDateTime> f, LinkDto linkDto) {
        if (response.isPresent()) {
            linkDto.setUpdatedAt(f.apply(response.get()));
            linkDto.setLastCheckedAt(OffsetDateTime.now());
        }
    }
}
