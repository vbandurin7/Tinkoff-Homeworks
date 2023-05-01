package ru.tinkoff.edu.java.scrapper.persistence.service.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.linkParser.parser.LinkParser;
import ru.tinkoff.edu.java.linkParser.parserResult.GitHubResult;
import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;
import ru.tinkoff.edu.java.linkParser.parserResult.StackOverflowResult;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkSaveRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.client.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowResponse;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static ru.tinkoff.edu.java.scrapper.schedule.utils.SchedulerUtils.getGitHubFunction;
import static ru.tinkoff.edu.java.scrapper.schedule.utils.SchedulerUtils.getStackoverflowFunction;

@Component
@RequiredArgsConstructor
public class LinkInfoUpdater {

    private final StackoverflowClient stackoverflowClient;
    private final GitHubClient gitHubClient;

    public void update(LinkSaveRequest lr) {
        ParseResult parseResult = LinkParser.parseURL(URI.create(lr.getDtoLink().getUrl()));
        if (parseResult instanceof GitHubResult pr) {
            Optional<GitHubResponse> gitHubResponse = gitHubClient.fetchRepository(pr.name(), pr.repository());
            if (gitHubResponse.isPresent()) {
                lr.getDtoLink().setLinkInfo(Map.of("username", pr.name(), "repository", pr.repository()));
                setTime(gitHubResponse, getGitHubFunction(), lr.getDtoLink());
            }
        } else if (parseResult instanceof StackOverflowResult pr) {
            Optional<StackoverflowResponse> stackoverflowResponse = stackoverflowClient.fetchQuestion(Long.parseLong(pr.id()));
            if (stackoverflowResponse.isPresent()) {
                lr.getDtoLink().setLinkInfo(Map.of("question", pr.id(), "answer_count", stackoverflowResponse.get().answerCount().toString()));
                setTime(stackoverflowResponse, getStackoverflowFunction(), lr.getDtoLink());
            }
        }
    }
    private <T> void setTime(Optional<T> response, Function<T, OffsetDateTime> f, LinkDto linkDto) {
        linkDto.setUpdatedAt(f.apply(response.get()));
        linkDto.setLastCheckedAt(OffsetDateTime.now());
    }
}
