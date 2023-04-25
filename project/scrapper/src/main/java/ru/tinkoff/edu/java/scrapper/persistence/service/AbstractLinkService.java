package ru.tinkoff.edu.java.scrapper.persistence.service;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import ru.tinkoff.edu.java.linkParser.parser.LinkParser;
import ru.tinkoff.edu.java.linkParser.parserResult.GitHubResult;
import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;
import ru.tinkoff.edu.java.linkParser.parserResult.StackOverflowResult;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.response.client.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowResponse;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.schedule.utils.SchedulerUtils;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static ru.tinkoff.edu.java.scrapper.schedule.utils.SchedulerUtils.getGitHubFunction;
import static ru.tinkoff.edu.java.scrapper.schedule.utils.SchedulerUtils.getStackoverflowFunction;


public abstract class AbstractLinkService implements LinkService {
    protected LinkRepository linkRepository;
    protected StackoverflowClient stackoverflowClient;
    protected GitHubClient gitHubClient;

    @Override
    public void save(Link link) {
        if (count(link.getUrl().toString()) == 0) {
            ParseResult parseResult = LinkParser.parseURL(URI.create(link.getUrl()));
            if (parseResult instanceof GitHubResult pr) {
                link.setLinkInfo(Map.of("username", pr.name(), "repository", pr.repository()));
                Optional<GitHubResponse> gitHubResponse = gitHubClient.fetchRepository(pr.name(), pr.repository());
                if (gitHubResponse.isPresent()) {
                    setTime(gitHubResponse, getGitHubFunction(), link);
                    linkRepository.save(link);
                }
            } else if (parseResult instanceof StackOverflowResult pr) {
                Optional<StackoverflowResponse> stackoverflowResponse = stackoverflowClient.fetchQuestion(Long.parseLong(pr.id()));
                if (stackoverflowResponse.isPresent()) {
                    link.setLinkInfo(Map.of("question", pr.id(), "answer_count", stackoverflowResponse.get().answerCount().toString()));
                    setTime(stackoverflowResponse, getStackoverflowFunction(), link);
                    linkRepository.save(link);
                }
            }
        }
    }

    @Override
    public void delete(String url) {
        linkRepository.deleteByUrl(url);
    }

    @Override
    public Link findByUrl(String url) {
        try {
            return linkRepository.findByUrl(url);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updateTime(Link link) {
        linkRepository.updateTime(link);
    }

    @Override
    public List<Link> findUnchecked() {
        return linkRepository.findUncheckedLinks();
    }

    @Override
    public long count(String url) {
        return linkRepository.countByUrl(url);
    }

    private <T> void setTime(Optional<T> response, Function<T, OffsetDateTime> f, Link link) {
        link.setUpdatedAt(f.apply(response.get()));
        link.setLastCheckedAt(OffsetDateTime.now());
    }
}
