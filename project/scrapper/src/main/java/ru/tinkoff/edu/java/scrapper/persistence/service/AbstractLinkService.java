package ru.tinkoff.edu.java.scrapper.persistence.service;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import ru.tinkoff.edu.java.linkParser.parser.LinkParser;
import ru.tinkoff.edu.java.linkParser.parserResult.GitHubResult;
import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;
import ru.tinkoff.edu.java.linkParser.parserResult.StackOverflowResult;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowResponse;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepository;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractLinkService implements LinkService {
    protected LinkRepository linkRepository;
    protected StackoverflowClient stackoverflowClient;

    @Override
    public void save(Link link) {
        if (count(link.getUrl().toString()) == 0) {
            ParseResult parseResult = LinkParser.parseURL(link.getUrl());
            if (parseResult instanceof GitHubResult pr) {
                link.setLinkInfo(Map.of("username", pr.name(), "repository", pr.repository()));
                linkRepository.save(link);
            } else if (parseResult instanceof StackOverflowResult pr) {
                Optional<StackoverflowResponse> stackoverflowResponse = stackoverflowClient.fetchQuestion(Long.parseLong(pr.id()));
                if (stackoverflowResponse.isPresent()) {
                    link.setLinkInfo(Map.of("question", pr.id(), "answer_count", stackoverflowResponse.get().answerCount().toString()));
                    linkRepository.save(link);
                }
            }
        }
    }
    @Override
    public void delete(URI url) {
        linkRepository.deleteByUrl(url.toString());
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
    public List<Link> findAll() {
        return linkRepository.findAll();
    }

    @Override
    public List<Link> findUnchecked() {
        return linkRepository.findUncheckedLinks();
    }

    @Override
    public long count(String url) {
        return linkRepository.countByUrl(url);
    }
}
