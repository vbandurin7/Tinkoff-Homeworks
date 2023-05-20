package ru.tinkoff.edu.java.scrapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.link_parser.parserResult.GitHubResult;
import ru.tinkoff.edu.java.link_parser.parserResult.ParseResult;
import ru.tinkoff.edu.java.link_parser.parserResult.StackOverflowResult;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.client.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowResponse;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.service.SubscriptionService;
import ru.tinkoff.edu.java.scrapper.persistence.service.rabbitmq.UpdateSender;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.function.Function;

import static ru.tinkoff.edu.java.scrapper.schedule.utils.SchedulerUtils.*;

@Component
@RequiredArgsConstructor
public class UpdateHandler {

    private final SubscriptionService subscriptionService;
    private final GitHubClient gitHubClient;
    private final StackoverflowClient stackoverflowClient;
    private final UpdateSender updateSender;

    public void handleUpdate(LinkDto linkDto, ParseResult parseResult) {
        String description = "";
        OffsetDateTime updatedBefore = linkDto.getUpdatedAt();
        if (parseResult instanceof GitHubResult pr) {
            Optional<GitHubResponse> gitHubResponse = gitHubClient.fetchRepository(pr.name(), pr.repository());
            updateLink(gitHubResponse, getGitHubFunction(), linkDto);
            description = "User <b>%s</b> has pushed a new commit at repository %s".formatted(pr.name(), linkDto.getUrl());
        } else if (parseResult instanceof StackOverflowResult pr) {
            Optional<StackoverflowResponse> stackoverflowResponse =
                stackoverflowClient.fetchQuestion(Long.parseLong(pr.id()));
            updateLink(stackoverflowResponse, getStackoverflowFunction(), linkDto);
            description = "New answers to the question with ID %s were left".formatted(pr.id());
        }
        if (updatedBefore.isBefore(linkDto.getUpdatedAt())) {
            updateSender.sendUpdate(new LinkUpdateRequest(linkDto.getId(), linkDto.getUrl(), description,
                subscriptionService.chatList(linkDto.getUrl()).stream().map(ChatDto::getId).toList()
            ));
        }
    }

    private <T> void updateLink(Optional<T> response, Function<T, OffsetDateTime> f, LinkDto linkDto) {
        if (response.isPresent()) {
            linkDto.setUpdatedAt(f.apply(response.get()).withOffsetSameInstant(ZoneOffset.ofHours(3)));
            linkDto.setLastCheckedAt(OffsetDateTime.now());
        }
    }
}
